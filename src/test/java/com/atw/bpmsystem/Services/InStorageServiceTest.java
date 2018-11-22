package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.DetailInStorage;
import com.atw.bpmsystem.Entities.InStorage;
import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Repositories.InStorageRepository;
import com.atw.bpmsystem.Repositories.StorageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InStorageServiceTest {
    @Autowired
    private InStorageService inStorageService;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private InStorageRepository inStorageRepository;
    @Test
    public void addInStorage() throws Exception {
//        InStorage inStorage=new InStorage();
//        List<DetailInStorage> detailInStorages=new ArrayList<>();
//            DetailInStorage detailInStorage=new DetailInStorage();
//            detailInStorage.setNumber(26);
//            detailInStorage.setPrice(4.3f);
//            Storage storage=storageRepository.getOne(1);
//            detailInStorages.add(detailInStorage);
//        inStorage.setDeliverer("deliverer");
//       // inStorage.setExeState(1);
//        inStorage.setRemark("remark");
//        inStorage.setOrderNumber("ordernumber");
//        inStorage.setRequestNo("requestNo");
//        inStorage.setDetailInStorages(detailInStorages);
//       // detailInStorage.setInStorageId(inStorage);
//      //  inStorageService.addInStorage(inStorage);
    }

    @Test
    public void addInStorageList() throws Exception {
    }
    @Test
    public void approvalInStorage() throws Exception {
    }
    @Test
    public void deleteInStorage() throws Exception {
        InStorage inStorage=inStorageRepository.getOne(13);

    }

    @Test
    public void fingAllInStorage() throws Exception {
    }

    @Test
    public void fingInStorageById() throws Exception {
    }

}