package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商实体
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Seller {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer sellerId;    //
    @Column(unique = true)
    private String sellerName;   //供应商名称
    private String contactor;    //联系人
    private String phone;        //联系电话
    private String email;        //联系邮箱
    private String address;      //地址
    private String sellingTypes; //经营范围
    @OneToMany(mappedBy="seller",cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Storage> storages=new ArrayList<>();
}
