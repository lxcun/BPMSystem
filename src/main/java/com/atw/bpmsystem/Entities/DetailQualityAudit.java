package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 质检清单实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetailQualityAudit {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "detailPurchase")
    private DetailPurchase detailPurchase;//采购物料清单
    private Integer sampleCount;          //检验数量
    private Integer passCount;            //通过数量
    private Float passRate;               //通过率
    private Integer inStorageCount;       //可入库数量
    private String batchNumber;           //批次号
    private LocalDate productionDate;                       //生产日期
    private LocalDate endDate;                              //有效期（失效日期）
    @ManyToOne
    @JoinColumn(name = "qualityAudit")
    private QualityAudit qualityAudit;
}
