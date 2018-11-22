package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 入库单实体
 */
//入库记录
@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InStorage implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int inStorageId;
    private String requestNo;               //入库单号
    private LocalDate inStoreDate;             //入库日期
    private String programNumber;           //项目号
    private String deliverer;               //入库人员（经办人）
    private String deliveryman;             //交货人
    private String storeageName;            //仓库名称
    private Integer type;                   //入库类型
    private String orderNumber;             //订单号
    private String remark;                  //备注
    @OneToMany(cascade = CascadeType.ALL,mappedBy="inStorage",fetch = FetchType.EAGER)
    private List<DetailInStorage> detailInStorages =new ArrayList<>();
    public void InStorage(){}
    public List<DetailInStorage> getDetailInStorages() {
        return detailInStorages;
    }
    @JsonManagedReference
    public void setDetailInStorages(List<DetailInStorage> detailInStorages) {
        this.detailInStorages = detailInStorages;
    }
}
