package com.tosee.tosee_writest.converter;
import com.tosee.tosee_writest.dataobject.PracticeRecord;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.utils.String2ListConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class PracticeRecord2PracticeRecordDTOConverter
{
    public static PracticeRecordDTO convert(PracticeRecord practiceRecord)
    {
        PracticeRecordDTO recordDTO  = new PracticeRecordDTO();

        BeanUtils.copyProperties(practiceRecord,recordDTO);

        // 字符串转数组：
        recordDTO.setUserAnswerList(String2ListConvertUtil.StringAnswer2ListAnswer(practiceRecord.getUserAnswerList()));


        return recordDTO;
    }
}
