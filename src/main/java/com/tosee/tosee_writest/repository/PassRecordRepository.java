package com.tosee.tosee_writest.repository;


import com.tosee.tosee_writest.dataobject.PassRecord;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:47 下午
 */
public interface PassRecordRepository extends JpaRepository<PassRecord, String>
{

}
