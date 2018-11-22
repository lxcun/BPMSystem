package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailQualityAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailQualityAuditRepository extends JpaRepository<DetailQualityAudit,Integer> {
    @Query(value = "select * from detail_quality_audit where  batch_number like ?1",nativeQuery = true)
    List<DetailQualityAudit> findByBatchNumberLike(String batchNumber);
}
