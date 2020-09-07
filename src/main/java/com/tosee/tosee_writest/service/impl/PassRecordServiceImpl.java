package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.PassRecord;
import com.tosee.tosee_writest.repository.PassRecordRepository;
import com.tosee.tosee_writest.service.PassRecordService;
import com.tosee.tosee_writest.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: FoxyWinner
 * @Date: 2020/9/5 10:02 下午
 */
@Slf4j
@Service
public class PassRecordServiceImpl implements PassRecordService
{
    @Autowired
    PassRecordRepository passRecordRepository;
    @Override
    public void commitPassRecord(PassRecord passRecord)
    {
        log.info("【出校记录】{}", passRecord);
        passRecord.setRecordId(KeyUtil.genUniqueKey());
        passRecordRepository.save(passRecord);
    }
}
