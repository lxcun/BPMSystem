package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Purchase;
import com.atw.bpmsystem.Entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseRepositoryTest {
    @Autowired
    private PurchaseRepository purchaseRepository;


//    @Test
//    public void findByCondition() throws Exception {
//        User requestor;
//        User auditor;
//        User approver;
//        Integer exeState;
//        Date createDate1;
//        Date createDate2;
//        Date auditDate1;
//        Date auditDate2;
//        Date approvalTime1;
//        Date approvalTime2;
//        Date orderDate1;
//        Date orderDate2;
//        Date receivingDate1;
//        Date receievingDate2;
//        Date inStorageDate1;
//        Date inStorageDate2;
//        Integer purchaseAccount1;
//        Integer purchaseAccount2;
//        Integer totalPrice1;
//        Integer totalPrice2;
//        Integer passRate1;
//        Integer passRate2;
//        List<Purchase> purchases=purchaseRepository.findByCondition(null,null,null,null,
//                null,null,null,null,
//                null,null,null,null,null,null,
//                null,null,null,null,null
//                ,null );
//        String ss="";
//    }

}