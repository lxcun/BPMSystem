package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OutStoreKeeper {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;
    @ManyToOne
    private User auditor;                  //出库人
    @ManyToOne
    private User requestor;                //领用人
    private String name;                   //名称
    private String programNumber;          //项目编号
    private String requestNo;              //出库单号
    private LocalDate outTime;                  //出库日期
    private Integer type;                  //出库类型
    private String storeageName;           //仓库名称
    private String department;             //领用部门
    private String remark;                 //备注
    private String outStorageNo;           //出库单号
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "out_storage")
    private OutStorage outStorage;//出库单
    @OneToMany(cascade = CascadeType.ALL,mappedBy="outStoreKeeper")
    private List<DetailOutStoreKeeper>detailOutStoreKeepers=new ArrayList<>();
    @JsonManagedReference
    public void setDetailOutStoreKeepers(List<DetailOutStoreKeeper> detailOutStoreKeepers) {
        this.detailOutStoreKeepers = detailOutStoreKeepers;
    }
}
