package com.tosee.tosee_writest.converter;

import com.tosee.tosee_writest.dataobject.Company;
import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.Question;
import com.tosee.tosee_writest.dto.ParentQuestionBankDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.repository.CompanyRepository;
import com.tosee.tosee_writest.service.CompanyService;
import com.tosee.tosee_writest.service.PositionService;
import com.tosee.tosee_writest.service.impl.CompanyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:28 下午
 */
@Slf4j
public class ParentQB2ParentQBDTOConverter
{
    @Autowired
    private PositionService positionService;
    public static ParentQuestionBankDTO convert(ParentQuestionBank parentQuestionBank)
    {
        ParentQuestionBankDTO parentQuestionBankDTO = new ParentQuestionBankDTO();

        BeanUtils.copyProperties(parentQuestionBank,parentQuestionBankDTO);
        //填入实体公司 行业 职位

//        if(parentQuestionBank.getCompanyId() != null)
//        {
//            Company company = companyService.findCompanyById(parentQuestionBank.getCompanyId());
//            parentQuestionBankDTO.setCompany(company);
//
//        }

//        parentQuestionBankDTO.setWorkField(positionService.findFieldByType(parentQuestionBank.getFieldType()));
//        parentQuestionBankDTO.setWorkPosition(positionService.findPositionByType(parentQuestionBank.getPositionType()));
        return parentQuestionBankDTO;
    }

    public static List<ParentQuestionBankDTO> convert(List<ParentQuestionBank> parentQuestionBanks)
    {
//        List<ParentQuestionBankDTO> result  = new ArrayList<>();
//        for (ParentQuestionBank parentQuestionBank : parentQuestionBanks)
//        {
//            result.add(convert(parentQuestionBank));
//        }
//        return result;
        return parentQuestionBanks
                .stream()
                .map(e -> convert(e))
                .collect(Collectors.toList());
    }
}
