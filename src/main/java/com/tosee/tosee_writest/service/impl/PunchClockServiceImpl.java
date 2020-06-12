package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.PunchClock;
import com.tosee.tosee_writest.repository.PunchClockRepository;
import com.tosee.tosee_writest.service.PunchClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/6/11 6:03 下午
 */
@Service
public class PunchClockServiceImpl implements PunchClockService
{
    @Autowired
    PunchClockRepository punchClockRepository;
    @Override
    public PunchClock findByOpenidAndPunchDate(String openid, Date punchDate)
    {
        return punchClockRepository.findByOpenidAndPunchDate(openid,punchDate);
    }
}
