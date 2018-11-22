package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 出库清单实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetailOutStorage implements Serializable {
    @Id
   // @GeneratedValue
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer detailOutStorageId;             //主键id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "out_storage")
    private OutStorage outStorage;//出库单
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material")
    private Material material;//库存
    private int number;//出库数量
    private String programNumber;//项目号
    @OneToMany(mappedBy = "detailOutStorage")
    private List<DetailPurchase>detailPurchases=new ArrayList<>();
    public void DetailOutStorage(){}
    @JsonBackReference
    public OutStorage getOutStorage() {
        return outStorage;
    }
    @JsonManagedReference
    public void setDetailPurchases(List<DetailPurchase> detailPurchases) {
        this.detailPurchases = detailPurchases;
    }
}
