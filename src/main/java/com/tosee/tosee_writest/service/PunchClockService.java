package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.PunchClock;

import java.sql.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/6/11 6:03 下午
 */
public interface PunchClockService
{
    PunchClock findByOpenidAndPunchDate(String openid, Date punchDate);
}
