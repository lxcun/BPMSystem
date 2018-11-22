package com.atw.bpmsystem.Models;

import com.atw.bpmsystem.Entities.InStorage;
import lombok.Data;

import java.util.List;
@Data
public class ListInStorageModel {
    private List<Integer> qualityAuditIdList;//质检单id列表
    private List<InStorageModel> inStorageModels;
}
