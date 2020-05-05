package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:53 下午
 */
@RestController
@RequestMapping("/writester/collect")
@Slf4j
public class WritesterCollectController
{
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResultVO addFavorite(@RequestParam("openid") String openid,
                                          @RequestParam("type") Integer type,
                                          @RequestParam("targetId") String targetId)
    {
        // 合法性判断
        if(StringUtils.isEmpty(openid))
        {
            log.error("【收藏夹】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        if(type == null)
        {
            log.error("【收藏夹】type为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(targetId))
        {
            log.error("【收藏夹】targetId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        favoriteService.addFavorite(openid,type,targetId);
        return ResultVOUtil.success();
    }

    @PostMapping("/cancel")
    public ResultVO cancelFavorite(@RequestParam("openid") String openid,
                                @RequestParam("type") Integer type,
                                @RequestParam("targetId") String targetId)
    {
        // 合法性判断
        if(StringUtils.isEmpty(openid))
        {
            log.error("【收藏夹】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        if(type == null)
        {
            log.error("【收藏夹】type为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(targetId))
        {
            log.error("【收藏夹】targetId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        favoriteService.cancelFavorite(openid,type,targetId);
        return ResultVOUtil.success();
    }
}
