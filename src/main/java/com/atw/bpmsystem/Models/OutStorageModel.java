package com.atw.bpmsystem.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OutStorageModel {
    private String programNumber;//项目号
    private String name;                   //名称（新加）
    private String requestNo;               //申请单号
    private String outStorageNo;           //出库单号
    private String auditor;                 //出库人
    private String requestor;               //领用人
    private String remark;                  //备注
    private LocalDate outTime;                //出库日期
    private Integer exeState;               //状态
    private Integer type;                   //出库类型
    private Integer typeNum;                //种类数
    private Float totalPrice;              //总金额
    private String outStorageProduct;       //出库商品
    private Integer outStorageId;           //出库单id
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
    private Integer totalNumber;            // 申请单中的出库数量
    private String closeReason;             //关闭原因：完成，审核拒绝，审批拒绝9.14号新加
    private Integer outStoreKeeperId;                     //outStoreKeeperId
    private Set<DetailStorageModel> outMaterial;
}
