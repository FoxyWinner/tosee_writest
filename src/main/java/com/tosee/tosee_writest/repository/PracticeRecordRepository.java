package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.PracticeRecord;
import com.tosee.tosee_writest.dataobject.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface PracticeRecordRepository extends JpaRepository<PracticeRecord, String>
{
    // 子题库列表 & 题目列表的"userAnwserList使用"
    PracticeRecord findByOpenIdAndAndChildQbId(String openid, String childQbId);

    // 练习记录界面
    List<PracticeRecord> findByOpenIdOrderByUpdateTimeDesc(String openid);
}
