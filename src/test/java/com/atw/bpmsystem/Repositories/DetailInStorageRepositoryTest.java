package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailInStorage;
import com.atw.bpmsystem.Entities.DetailOutStorage;
import com.atw.bpmsystem.Entities.Storage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DetailInStorageRepositoryTest {
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private DetailInStorageRepository detailInStorageRepository;
    @Test
    public void findByBatchNumberAndStorage() throws Exception {
//       Storage storage=storageRepository.getOne(7);
       // DetailInStorage detailInStorage=detailInStorageRepository.findByBatchNumberAndStorage("as",storage);
    }

}