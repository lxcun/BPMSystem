package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 入库清单实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetailInStorage implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer detailInStorageId;             //主键id
    @ManyToOne(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinColumn(name = "in_storage")
    private InStorage inStorage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="material")
    private Material material;
    private float price;//单价
    private int number;//入库数量
    private String batchNumber;//批次号
    private String programNumber;//项目号
    private String detailPos;//存放位置
    private String produceFactoryName;//生产厂家
    private String seller;//销售商

    public void DetailInStorage(){}

    public void DetailInStorage(float price,int number){
        this.number=number;
        this.price=price;
    }
    @JsonBackReference
    public InStorage getInStorage() {
        return inStorage;
    }
}
