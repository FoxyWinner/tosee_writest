package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.converter.PracticeRecord2PracticeRecordDTOConverter;
import com.tosee.tosee_writest.dataobject.PracticeRecord;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.repository.PracticeRecordRepository;
import com.tosee.tosee_writest.service.PracticeRecordService;
import com.tosee.tosee_writest.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 9:03 下午
 */
@Service
public class PracticeRecordServiceImpl implements PracticeRecordService
{
    @Autowired
    private PracticeRecordRepository practiceRecordRepository;
    @Override
    public void addOrUpdateRecord(PracticeRecordDTO practiceRecordDTO)
    {
        // 先查数据库里有没有这条子题库的做题记录，如果有的话取出来然后更新，如果没有才新建
        PracticeRecord practiceRecord = practiceRecordRepository.findByOpenIdAndAndChildQbId(practiceRecordDTO.getOpenid(),practiceRecordDTO.getChildQbId());

        // 更新
        if(practiceRecord != null)
        {
            practiceRecord.setComplete(practiceRecordDTO.getComplete());
            practiceRecord.setCompleteNumber(practiceRecordDTO.getCompleteNumber());
            practiceRecord.setSpentTime(practiceRecordDTO.getSpentTime());
            // 如果格式不对我们可以封装一个把List<String>转化为数组字符串的方法
            practiceRecord.setUserAnswerList(practiceRecordDTO.getUserAnswerList().toString());
            practiceRecord.setCorrectRatio(practiceRecordDTO.getCorrectRatio());
            practiceRecord.setLastMode(practiceRecordDTO.getLastMode());

            practiceRecordRepository.save(practiceRecord);
        }
        //新建
        else
        {
            practiceRecord = new PracticeRecord();
            practiceRecord.setRecordId(KeyUtil.genUniqueKey());
            practiceRecord.setOpenId(practiceRecordDTO.getOpenid());
            practiceRecord.setChildQbId(practiceRecordDTO.getChildQbId());
            practiceRecord.setComplete(practiceRecordDTO.getComplete());
            practiceRecord.setCompleteNumber(practiceRecordDTO.getCompleteNumber());
            practiceRecord.setSpentTime(practiceRecordDTO.getSpentTime());
            // 如果格式不对我们可以封装一个把List<String>转化为数组字符串的方法
            practiceRecord.setUserAnswerList(practiceRecordDTO.getUserAnswerList().toString());
            practiceRecord.setCorrectRatio(practiceRecordDTO.getCorrectRatio());
            practiceRecord.setLastMode(practiceRecordDTO.getLastMode());

            // TODO surpassRatio怎么运算
            practiceRecord.setSurpassRatio(0);

            practiceRecordRepository.save(practiceRecord);
        }
    }

    @Override
    public PracticeRecordDTO findRecord(String openid, String cqbId)
    {
        PracticeRecord practiceRecord = practiceRecordRepository.findByOpenIdAndAndChildQbId(openid,cqbId);
        PracticeRecordDTO recordDTO = PracticeRecord2PracticeRecordDTOConverter.convert(practiceRecord);
        return recordDTO;
    }
}
