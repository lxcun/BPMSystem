package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.DetailOutStorage;
import com.atw.bpmsystem.Entities.OutStorage;
import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Repositories.OutStorageRepository;
import com.atw.bpmsystem.Repositories.StorageRepository;
import com.atw.bpmsystem.Services.OutStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OutStorageServiceImplTest {



    @Autowired
    private OutStorageService outStorageService;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private OutStorageRepository outstorageRepository;
//    @Test
//    public void saveOutStorage() throws Exception {
//        Principal principal=new Principal() {
//            @Override
//            public String getName() {
//               return "liao";
//            }
//        };
//        OutStorage outStorage=outstorageRepository.getOne(2);
//        OutStorageModel outStorageModel=outStorageService.outStorageToModel(outStorage);
//       outStorageService.saveOutStorage(outStorageModel,principal);
//    }
//    @Test
//    public void addOutStorage() throws Exception {
//        OutStorage outStorage=new OutStorage();
//        List<DetailOutStorage> detailoutStorages=new ArrayList<>();
//        DetailOutStorage detailoutStorage=new DetailOutStorage();
//        detailoutStorage.setNumber(26);
////        detailoutStorage.setPrice(4.3f);
//        Storage storage=storageRepository.getOne(1);
//        detailoutStorages.add(detailoutStorage);
//        outStorage.setRemark("remark");
//        outStorage.setRequestNo("requestNo");
//        outStorage.setRequestor("requstor");
//        outStorage.setAuditor("auditor");
//        outStorage.setDetailOutStorages(detailoutStorages);
//      //  outStorageService.addOutStorage(outStorage);
//    }

    @Test
    public void addOutStorageList() throws Exception {
    }
    @Test
    public void approvalOutStorage() throws Exception {
      //  outStorageService.approvalOutStorage(1,3,"审核通过");
    }
    @Test
    public void deleteOutStorage() throws Exception {
    }

    @Test
    public void findAllOutStorage() throws Exception {
    }

    @Test
    public void findOutStorageById() throws Exception {
    }

}