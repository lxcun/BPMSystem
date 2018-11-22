package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购清单实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetailPurchase implements Serializable {
    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int detailPurchaseId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "detailOutStorage")
    private DetailOutStorage detailOutStorage;
    @ManyToOne
    @JoinColumn(name = "material")
    private Material material;
    @ManyToOne()
    @JoinColumn(name = "purchase")
    @JsonIgnore
    private Purchase purchase;
    @ManyToOne
    private Seller seller;
    @OneToMany(mappedBy = "detailPurchase")
    @JsonIgnore
    private List<DetailQualityAudit> detailQualityAudits=new ArrayList<>();
    private String programNumber;     //项目号
    private String replaceModel;      //替代型号
    private String purchaseModel;     //实际采购型号
    private Integer reqCount;         //申请数量
    private Float reqPrice;           //申请单价
    private Integer purchaseCount;    //采购数量
    private Float purchasePrice;      //采购单价
    private String remark;            //备注
    private String orderId;           //订单号
    private String contractNumber;     //合同编号
    private LocalDate expirationDate;  //货期
    public void DetailPurchase(){}
    @JsonBackReference
    public DetailOutStorage getDetailOutStorage() {
        return detailOutStorage;
    }

}
