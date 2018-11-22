package com.atw.bpmsystem.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DetailPurchaseModel {
    private Integer detailPurchaseId;
    private String name;              //物料名称
    private String code;              //物料编码
    private String modelNumber;       //型号
    private Integer materialId;       //物料id
    private String replaceModel;      //替代型号
    private String purchaseModel;     //实际采购型号
    private String encapsulation;     //封装
    private String sellerName;        //供应商
    private Integer reqCount;         //申请数量
    private Float reqPrice;           //申请单价
    private Integer purchaseCount;    //采购数量
    private Float purchasePrice;      //采购单价
    private Float totalPrice;         //总金额
    private String programNumber;
    private String orderId;                 //订单号
    private Integer detailOutStorageId;   //出库单号
    private Float passRate;               //质检通过率
    private Integer inStorageCount;       //可入库数量
    private String remark;            //备注
    private String contractNumber;     //合同编号
    private LocalDate expirationDate;  //货期
    private String materialType;           //物料类型名称

}
