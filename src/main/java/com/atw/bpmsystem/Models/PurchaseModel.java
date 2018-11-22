package com.atw.bpmsystem.Models;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class PurchaseModel {
    private LocalDate creatDate;                 //创建申请日期
    private Integer purchaseId;       //采购id
    private String name;              //采购单名称
    private LocalDate endDate;             //要求到货日期
    private Integer exeState;               //执行状态
    private String auditAdvice;             //审核意见
    private String approvalAdvice;          //审批意见
    private String approver;                //审批人
    private Integer typeNum;                //种类数
    private Float totalPrice;              //总金额
    private Integer totalAmount;            //总数量

    private String requestor; //申请人
    private String auditor;  //审核人
    private LocalDate orderDate;   //完成采购日期（采购日期）
    private LocalDate receivingDate;  //通知质检日期（到货日期）
    private LocalDate qADate;        //质检完成日期
    private LocalDate inStorageDate;  //入库日期


    private List<DetailPurchaseModel> detailPurchaseModels;
}
