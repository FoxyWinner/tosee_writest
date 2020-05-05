package com.tosee.tosee_writest.converter;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.form.RecordForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RecordForm2PracticeRecordDTOConverter
{
    public static PracticeRecordDTO convert(RecordForm recordForm)
    {
        PracticeRecordDTO recordDTO  = new PracticeRecordDTO();

        recordDTO.setOpenid(recordForm.getOpenid());
        recordDTO.setChildQbId(recordForm.getCqbId());
        recordDTO.setComplete(recordForm.getComplete());
        recordDTO.setCompleteNumber(recordForm.getCompleteNumber());
//        recordDTO.setRecordId();
        recordDTO.setSpentTime(recordForm.getSpentTime());
        recordDTO.setLastMode(recordForm.getMode());
        recordDTO.setUserAnswerList(recordForm.getUserAnswerList());
        recordDTO.setCorrectRatio(recordForm.getCorrect());

        return recordDTO;
    }
}
