package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购实体
 */
//采购记录
@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Purchase implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer purchaseId;
    @ManyToOne
    private User requestor;               //申请人
    @ManyToOne
    private User auditor;                 //审核人
    @ManyToOne
    private User approver;                //审批人
    private LocalDate endDate;                   //要求到货日期
    private LocalDate creatDate;                 //创建申请日期
    private LocalDate submitDate;                //提交时间
    private Integer exeState;               //执行状态
    private String auditAdvice;             //审核意见
    private String approvalAdvice;          //审批意见
    private LocalDate auditDate;                 //审核日期
    private LocalDate approvalTime;              //审批日期
    private LocalDate orderDate;                 //完成采购日期
    private LocalDate receivingDate;             //通知质检日期
    private LocalDate qADate;                    //质检完成日期
    private LocalDate inStorageDate;             //入库日期
    private String name;                    //采购单名称
    @OneToMany(mappedBy="purchase",cascade = CascadeType.REMOVE)
    private List<OutStorage>outStorages=new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="qualityAudit")
    private QualityAudit qualityAudit;       //质检单
    @OneToMany(cascade = CascadeType.ALL,mappedBy="purchase")
    private List<DetailPurchase> detailPurchases=new ArrayList<>();
    public void Purchase(){}
    public List<DetailPurchase> getDetailPurchases() {
        return detailPurchases;
    }
    @JsonManagedReference
    public void setDetailPurchases(List<DetailPurchase> detailPurchases) {
        this.detailPurchases = detailPurchases;
    }

}
