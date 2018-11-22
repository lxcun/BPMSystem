package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 类型实体
 */
@Entity
@Getter
@Setter
public class Type {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer typeId;    //种类Id
    @Column(unique = true)
    private String typeName;   //种类名称
    @OneToMany(mappedBy="type")
    @JsonIgnore
    private List<Material> materials;
    public void Type(){}
}
