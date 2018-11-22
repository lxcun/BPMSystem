package com.atw.bpmsystem.Models;

import com.atw.bpmsystem.Entities.DetailQualityAudit;
import com.atw.bpmsystem.Entities.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QualityAuditModel {
    private Integer qualityAuditId; //qualityAuditId
    private String qaName;               //质检员
    private String name;           //质检单名称
    private String remark;         //备注
    private Integer state;         //状态
    private Boolean result;        //结果
    private Integer purchaseId;     //采购单号
    private LocalDate orderDate;     //完成采购日期(采购日期）
    private Integer inStorageCount;  //物料数量
    private Float  inStoragePrice;   //总金额
    private String requestor;        //采购员
    private LocalDate receivingDate; //通知质检日期（到货日期）
    private List<DetailQualityAuditModel> detailQualityAuditModels;
}
