package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 质检单实体
 */
@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QualityAudit {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer qualityAuditId;//质检单id
    @ManyToOne
    private User qa;               //质检员
    private LocalDate auditDate;        //质检完成日期
    private String name;           //质检单名称
    private String remark;         //备注
    private Integer state;         //状态
    private Boolean result;        //结果
    @JsonIgnore
    @OneToMany(mappedBy = "qualityAudit")
    private List<Purchase> purchases;
    @JsonIgnore
    @OneToMany(mappedBy = "qualityAudit",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<DetailQualityAudit> detailQualityAudits=new ArrayList<>();
}
