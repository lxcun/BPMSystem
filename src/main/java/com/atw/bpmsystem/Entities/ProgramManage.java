package com.atw.bpmsystem.Entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ProgramManage {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer programNumberId;
    private String programName;
    private String programNumber;
    private String remark;
}
