package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Feedback;
import com.tosee.tosee_writest.dataobject.PunchClock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:49 下午
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String>
{

}
