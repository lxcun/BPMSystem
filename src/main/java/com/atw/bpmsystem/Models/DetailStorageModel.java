package com.atw.bpmsystem.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailStorageModel {
    private String name;//物料名称(出入库）（传入和传出）
    private String modelNumber;//型号（出入库）（传入和传出）
    private String code;//编码（出入库）（传入和传出）
    private Integer amount;//库存数量（入/出库单返回）
    private Float price;//最新单价（出入库）（传入和传出）
    private Float total;//amount*price（出入库）（传入和传出）
    private String remark; //备注（出入库）（传入和传出）
    private Integer materialId;
    private String sequence;//批次号（出入库）（传入和传出）
    private String programNumber;//项目号（出入库）（传入和传出）
    private String detailPos;//存放位置（入库）（传入和传出）
    private String produceFactoryName;//生产厂家（入库）（传入和传出）
    private Integer detailInStorageId;
    private Integer detailOutStorageId;
    private Integer miniumTemp;                             //最低工作温度（传入和传出）
    private Integer maxTemp;                                //最高工作温度（传入和传出）
    private String encapsulation;                           //封装（传入和传出）
    private String materialType;                            //物料所属类型（传入和传出）
    private Integer number;                                 //入库单传出（入库单的物料数量）
    private String sellerName;
}
