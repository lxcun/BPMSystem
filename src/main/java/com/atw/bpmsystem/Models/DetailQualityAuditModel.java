package com.atw.bpmsystem.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DetailQualityAuditModel {
    private Integer id;               //detailQualityAuditId
    private String name;              //物料名称
    private String code;              //物料编码
    private Integer materialId;       //物料id
    private String purchaseModel;     //实际采购型号
    private String encapsulation;     //封装
    private String sellerName;        //供应商
    private Integer purchaseCount;    //采购数量
    private Float purchasePrice;      //采购单价
    private Integer sampleCount;          //检验数量
    private Integer passCount;            //通过数量
    private Float passRate;               //通过率
    private Integer inStorageCount;       //可入库数量
    private String batchNumber;           //批次号
    private Integer detailPurchaseId;     //采购清单
    private String programNumber;        //项目号
    private String orderId;              //订单号
    private String remark;            //备注
    private String contractNumber;     //合同编号
    private LocalDate expirationDate;  //货期
    private LocalDate productionDate;                       //生产日期
    private LocalDate endDate;                              //有效期（失效日期）

}
