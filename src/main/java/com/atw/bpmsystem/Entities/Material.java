package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Material {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
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
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name ="type")
    private Type type;                                      //物料所属类型
    private Integer shelfLife;                              //保质期
    @OneToMany(mappedBy="material",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Storage> storages=new ArrayList<>();
    @OneToMany(mappedBy="material",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<DetailOutStorage> detailOutStorages=new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy="material",cascade = CascadeType.REMOVE)
    private List<DetailInStorage> detailInStorage =new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy="material",cascade = CascadeType.REMOVE)
    private List<DetailPurchase> detailPurchases=new ArrayList<>();
    @OneToMany(mappedBy="material",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<DetailOutStoreKeeper> detailOutStoreKeepers=new ArrayList<>();

}
