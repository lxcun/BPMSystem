package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Seller;
import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Repositories.MaterialRepository;
import com.atw.bpmsystem.Repositories.SellerRepository;
import com.atw.bpmsystem.Repositories.StorageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageServiceImplTest {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Test
    public void addStorage() throws Exception {
//        Storage storage=new Storage();
//        Seller seller=sellerRepository.getOne(1);
//        storage.setProduceFactoryName("富士康公司");
//        Material material=materialRepository.getOne(1);
//        storage.setAmount(3456);
//        storage.setPrice(15.2f);
//        storage.setEndDate(LocalDate.now());
//        storage.setStoreageName("ahksdfh");
//        storage.setSequence("khashfd");
//        storage.setSeller(seller);
//        storage.setMaterial(material);
//        storageRepository.save(storage);
    }

    @Test
    public void addStorageList() throws Exception {
    }

    @Test
    public void deleteStorage() throws Exception {

    }

    @Test
    public void modifyStorage() throws Exception
    {
//        Storage storage=new Storage();
//        storage.setAmount(55);
//        storage.setPrice(2.24f);
//        storage.setProduceFactoryName("sdf");
    }

    @Test
    public void findAllStorage() throws Exception {
    }

    @Test
    public void findStorageById() throws Exception {
    }

}