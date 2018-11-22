package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.QualityAudit;
import com.atw.bpmsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QualityAuditRepository extends JpaRepository<QualityAudit,Integer> {
    /**通过质检员和状态查询质检
     * @param qa
     * @param state
     * @return
     */
    List<QualityAudit>findByQaAndState(User qa,Integer state);

    /**通过质检结果查找质检
     * @param result
     * @return
     */
    List<QualityAudit>findByResult(Boolean result);

    /**通过质检结果和状态查询质检
     * @param result
     * @param state
     * @return
     */
    List<QualityAudit>findByResultAndState(Boolean result,Integer state);
}
