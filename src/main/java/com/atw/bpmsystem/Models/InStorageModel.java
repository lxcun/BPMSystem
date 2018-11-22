package com.atw.bpmsystem.Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class InStorageModel {
    private Integer inStorageId;            //入库id
    private LocalDate inStoreDate;               //入库日期
    private String programNumber;           //项目号
    private String deliverer;               //入库人员
    private String remark;                  //备注
    private String requestNo;               //入库单号
    private Integer type;                   //入库类型
    private String storeageName;            //仓库名称
    private String deliveryman;             //交货人
    private String orderNumber;             //订单号
    private List<DetailStorageModel> list;


    }
