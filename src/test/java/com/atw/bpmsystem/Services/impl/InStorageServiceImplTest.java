package com.atw.bpmsystem.Services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InStorageServiceImplTest {
    @Autowired
    private  InStorageServiceImpl inStorageService;
    @Test
    public void deleteInStorage() throws Exception {
//        List<Map<String,Integer>> idList=new ArrayList<>();
//        Map<String,Integer> map=new HashMap<>();
//        map.put("id",2);
//        idList.add(map);
//        inStorageService.deleteInStorage(idList);
    }

}