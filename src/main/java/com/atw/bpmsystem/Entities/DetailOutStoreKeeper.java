package com.atw.bpmsystem.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetailOutStoreKeeper {
    @Id
    // @GeneratedValue
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "out_store_keeper")
    private OutStoreKeeper outStoreKeeper;//出库单
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material")
    private Material material;//库存
    private Float price;//单价
    private Integer number;//出库数量
    private String batchNumber;//批次号
    private String ProgramNumber;//项目号
    @JsonBackReference
    public OutStoreKeeper getOutStoreKeeper() {
        return outStoreKeeper;
    }
}
