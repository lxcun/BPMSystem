package com.atw.bpmsystem.Models;

import lombok.Data;

@Data
public class MaterialModel {
    private int materialId;
    private String remark;                                  //备注
    private String name;                                    //物料名称
    private String preCode;                                 //物料编码前缀
    private String code;                                    //物料编码
    private Boolean roHS;                                   //是否符合RoHS标准
    private Boolean active;                                 //物料状态
    private String description;                             //物料描述
    private String modelNumber;                             //型号
    private Integer miniumTemp;                             //最低工作温度
    private Integer maxTemp;                                //最高工作温度
    private String encapsulation;                           //封装
    private String typeName;
    private int amount;                                     //数量（库存）
    private float price;                                    //单价（最新）
    private Integer shelfLife;                              //保质期
}
