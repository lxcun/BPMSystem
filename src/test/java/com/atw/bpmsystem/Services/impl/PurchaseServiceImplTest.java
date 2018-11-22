package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.Purchase;
import com.atw.bpmsystem.Models.FindPurchaseModel;
import com.atw.bpmsystem.Services.PurchaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseServiceImplTest {
    @Autowired
    private PurchaseService purchaseService;
    @Test
    public void findCondition() throws Exception {
//        FindPurchaseModel findPurchaseModel=new FindPurchaseModel();
//        List<FindPurchaseModel>findPurchaseModels=new ArrayList<>();
//        findPurchaseModel.setName("exe_state");
//        findPurchaseModel.setOperation("equal");
//        findPurchaseModel.setSmall("7");
//        findPurchaseModel.setLarge("2");
//        findPurchaseModels.add(findPurchaseModel);
//        Map<String, Object> modelMap=purchaseService.findCondition(findPurchaseModels);
//        String s="";
    }

}