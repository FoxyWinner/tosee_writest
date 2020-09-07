package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.MistakeBook;
import com.tosee.tosee_writest.dataobject.PassRecord;
import com.tosee.tosee_writest.dto.MistakeBookDTO;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 9:50 下午
 */
public interface PassRecordService
{
    /**
     * 提交表单
     * @param passRecord
     */
    void commitPassRecord(PassRecord passRecord);
}
