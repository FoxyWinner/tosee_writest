package com.tosee.tosee_writest.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.tosee.tosee_writest.dataobject.ArticleTag;
import com.tosee.tosee_writest.dataobject.ExperienceArticle;
import com.tosee.tosee_writest.dataobject.PunchClock;
import com.tosee.tosee_writest.dto.UserDTO;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.PunchStateEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.PunchClockRepository;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.KeyUtil;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.dc.pr.PRError;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:00 下午
 */
@RestController
@RequestMapping("/writester/punch")
@Slf4j
public class WritesterPunchController
{
    @Autowired
    PunchClockService punchClockService;

    @Autowired
    UserService userService;

    @Autowired
    PunchClockRepository punchClockRepository;

    @GetMapping("/getstate")
    public ResultVO getState(@RequestParam("openid") String openid)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【获取打卡状态】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        log.info("【获取打卡状态】{}",date);
        PunchClock punchClock =punchClockService.findByOpenidAndPunchDate(openid,date);


        PunchResultVO result = new PunchResultVO();
        // 完成题目数 今日正确率 今日总时长

        if (punchClock == null)
        {
            log.info("【获取打卡状态】打卡记录为空，自动填充初始化打卡状态");
            result.setPunchState(PunchStateEnum.NOT_UP_TO_STANDARD_AND_NOT_CLOCKIN.getCode());
            result.setCompleteNumber(0);
            result.setCorrectRatio(0);
            Integer allSeconds = 0;
            result.setExerciseTime(this.getExerciseTimeStr(allSeconds));
            result.setInsistDays(punchClockRepository.countByOpenidAndPunchState(openid,PunchStateEnum.ALREADY_CLOCKIN.getCode()));

        }
        else
        {
            result.setPunchState(punchClock.getPunchState());
            result.setCompleteNumber(punchClock.getCompleteNumber());
            if (punchClock.getCompleteNumber() > 0 && punchClock.getSolveNumber() > 0)
            {
                result.setCorrectRatio(punchClock.getSolveNumber() * 100 / punchClock.getCompleteNumber());
            }else
            {
                result.setCorrectRatio(0);
            }
            Integer allSeconds = punchClock.getExerciseTime();
            result.setExerciseTime(this.getExerciseTimeStr(allSeconds));
            result.setInsistDays(punchClockRepository.countByOpenidAndPunchState(openid,PunchStateEnum.ALREADY_CLOCKIN.getCode()));
        }
        return ResultVOUtil.success(result);
    }

    // 这个方法应该弃用 因为只要提交了complete为1的记录，那么打卡状态就为2
    // todo 暂时只作为前端调试用，先不删除了
    @GetMapping("/setstate")
    public ResultVO setState(@RequestParam("openid") String openid,
                            @RequestParam("punchState") Integer punchState)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【更改打卡状态】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        // 先查找再更改或直接新建
        PunchClock punchClock =punchClockService.findByOpenidAndPunchDate(openid,new java.sql.Date(System.currentTimeMillis()));

        if (punchClock == null)
        {
            // 新建
            punchClock = new PunchClock();
            punchClock.setPunchId(KeyUtil.genUniqueKey());
            punchClock.setOpenid(openid);
            punchClock.setPunchState(PunchStateEnum.NOT_UP_TO_STANDARD_AND_NOT_CLOCKIN.getCode());
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            punchClock.setPunchDate(date);

            punchClock.setCompleteNumber(0);
            punchClock.setSolveNumber(0);
            punchClock.setExerciseTime(0);
        }
        else
        {
            punchClock.setPunchState(punchState);
        }
        punchClockRepository.save(punchClock);
        return ResultVOUtil.success();
    }

    /**
     * 打卡接口 前端可自行检查用户授权状态和打卡状态后调用该接口
     * 实际上做完一套题提交结果报告后必可打卡 此时前端只需校验用户是否已授权
     * @param openid
     * @return
     */
    @Transactional
    @GetMapping("/punchclock")
    public ResultVO punchClock(@RequestParam("openid") String openid)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【打卡】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        UserDTO userDTO;
        userDTO = userService.getUserInfo(openid);

        if (userDTO  == null || StringUtils.isEmpty(userDTO.getProfilePhotoUrl()))
        {
            log.error("【打卡】找不到该用户的头像信息");
            throw new WritestException(ResultEnum.USERINFO_NOT_EXIST);
        }

        PunchClock punchClock =punchClockService.findByOpenidAndPunchDate(openid,new java.sql.Date(System.currentTimeMillis()));
        if (punchClock == null)
        {
            log.info("【打卡失败】不满足打卡条件，今天无任何记录");
            return ResultVOUtil.error(ResultEnum.NOT_UP_TO_CLOCKING_STANDARD.getCode(),ResultEnum.NOT_UP_TO_CLOCKING_STANDARD.getMessage());
        }else if (punchClock.getPunchState() == PunchStateEnum.NOT_UP_TO_STANDARD_AND_NOT_CLOCKIN.getCode())
        {
            log.info("【打卡失败】不满足打卡条件{}，",PunchStateEnum.NOT_UP_TO_STANDARD_AND_NOT_CLOCKIN.getMessage());
            return ResultVOUtil.error(ResultEnum.NOT_UP_TO_CLOCKING_STANDARD.getCode(),ResultEnum.NOT_UP_TO_CLOCKING_STANDARD.getMessage());
        }

        PunchResultVO result = new PunchResultVO();
        if (punchClock.getPunchState() == PunchStateEnum.ALREADY_CLOCKIN.getCode())
            result.setIsFirstTimePunch(0);
        else
            result.setIsFirstTimePunch(1);

        // 是可以重复打卡的
        log.info("【打卡成功】{}",punchClock);

        // 置打卡位，存储打卡信息，若是重复打卡，那我们不用再多存一遍了
        if(punchClock.getPunchState() != PunchStateEnum.ALREADY_CLOCKIN.getCode())
        {
            punchClock.setPunchState(PunchStateEnum.ALREADY_CLOCKIN.getCode());
            punchClockRepository.save(punchClock);
        }


        // 完成题目数 今日正确率 今日总时长
        result.setPunchState(punchClock.getPunchState());
        result.setCompleteNumber(punchClock.getCompleteNumber());
        if (punchClock.getCompleteNumber() > 0 && punchClock.getSolveNumber() > 0)
        {
            result.setCorrectRatio(punchClock.getSolveNumber() * 100 / punchClock.getCompleteNumber());
        }else
        {
            result.setCorrectRatio(0);
        }
        Integer allSeconds = punchClock.getExerciseTime();
        result.setExerciseTime(this.getExerciseTimeStr(allSeconds));
        result.setProfilePhoto(userDTO.getProfilePhotoUrl());
        result.setUserName(userDTO.getUserName());
        result.setInsistDays(punchClockRepository.countByOpenidAndPunchState(openid,PunchStateEnum.ALREADY_CLOCKIN.getCode()));

        return ResultVOUtil.success(result);
    }


    public String getExerciseTimeStr(Integer seconds) {
        //秒数
        if (seconds < 60)
            return "0分钟";
		if (seconds < 3600)
		    return Math.round(seconds / 60) + "分钟";

		int hour = Math.round(seconds / 3600);
		int minute = Math.round((seconds - (hour * 3600)) / 60);
		return hour + "小时" + (minute == 0 ? "" : minute + "分钟");
    }

}
