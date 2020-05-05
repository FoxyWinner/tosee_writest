package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.PracticeRecord;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:21 下午
 */
public interface PracticeRecordService
{
    void addOrUpdateRecord(PracticeRecordDTO practiceRecordDTO);

    PracticeRecordDTO findRecord(String openid, String cqbId);
}
