package com.atw.bpmsystem.Models;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class FindPurchaseModel {
    private String name;             //参数名
    private String operation;        //操作
    private String small;            //值(下界）
    private String large;            //上界
}
