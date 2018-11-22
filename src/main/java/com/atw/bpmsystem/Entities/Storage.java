package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * 库存实体
 */
@Entity
@Getter @Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="detailInStorage")
//库存记录
public class Storage implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer storageId;
    private Integer amount;                                     //数量(库存）
    private Float price;                                    //单价
    private LocalDate endDate;                              //有效期（失效日期）
    private LocalDate productionDate;                       //生产日期
    private String storeageName;                            //仓库名称
    private String produceFactoryName;                      //生产厂商
    private String sequence;                                //批次号
    private String programNumber;                           //项目号
    @ManyToOne
    @JoinColumn(name ="seller")
    private Seller seller;                              //供应商
    @ManyToOne
    @JoinColumn(name ="material")
    private Material material;


    public void Storage(){}
}
