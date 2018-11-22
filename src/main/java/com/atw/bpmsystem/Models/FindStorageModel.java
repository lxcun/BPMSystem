package com.atw.bpmsystem.Models;

import lombok.Data;

@Data
public class FindStorageModel {
    private String code;                                    //物料编码
    private String modelNumber;                             //型号
    private String encapsulation;                           //封装
    private String type;                                    //类型
    private String programNumber;                           //项目号
}
