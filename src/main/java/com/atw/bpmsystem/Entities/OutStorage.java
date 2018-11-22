package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
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
 * 出库实体
 */
@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//出库记录
public class OutStorage implements Serializable {
    @Id
   // @GeneratedValue
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer OutStorageId;
    private String programNumber;//项目号
    private String name;                    //名称（新加）
    private String requestNo;               //申请单号
    private String auditor;                 //出库人
    private String requestor;               //领用人
    private String remark;                  //备注
    private LocalDate outTime;                //出库日期
    private Integer exeStat;                //执行状态
    private Integer type;                   //出库类型
    private Float totalPrice;               //出库总金额
    private String storeageName;            //仓库名称
    private String department;              //所属部门
    private String brderNumber;             //订单号
    private String examiner;                //审核人
    private String approver;                //审批人
    private LocalDate createDate;                //创建日期
    private LocalDate auditDate;                 //审核日期
    private LocalDate approveDate;               //审批日期
    private String histories;               //状态变化历史
    private String auditComments;           //审核意见与建议
    private String approveComments;         //审批意见与建议
    private String closeReason;             //关闭原因：完成，审核拒绝，审批拒绝9.14号新加
    @JsonIgnore
    @OneToMany(mappedBy="outStorage")
    private List<OutStoreKeeper> outStoreKeepers=new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy="outStorage")
    private List<DetailOutStorage> detailOutStorages=new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase")
    private Purchase purchase;
    public void OutStorage(){}
    public List<DetailOutStorage> getDetailOutStorages() {
        return detailOutStorages;
    }
    @JsonManagedReference
    public void setDetailOutStorages(List<DetailOutStorage> detailOutStorages) {
        this.detailOutStorages = detailOutStorages;
    }

}
