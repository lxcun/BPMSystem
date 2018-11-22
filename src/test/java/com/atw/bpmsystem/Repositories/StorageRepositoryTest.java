package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Entities.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageRepositoryTest {
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private TypeRepository typeRepository;
@Test
    public void findByConditionTest(){
//    Type type=typeRepository.getOne(1);

}


}