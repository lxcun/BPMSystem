package com.atw.bpmsystem.Models;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class FindOutStorageModel {
   private Integer type;
   private String department;
   private String programNumber;
   private String requestor;
   private LocalDate startTime;
   private LocalDate endTime;
}
