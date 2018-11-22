package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Type;
import com.atw.bpmsystem.Repositories.MaterialRepository;
import com.atw.bpmsystem.Repositories.TypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaterialServiceTest {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private TypeRepository typeRepository;
    @Test
    public void addMaterial() throws Exception {
//        Material material=new Material();
//        material.setRoHS(true);
//        material.setModelNumber("asdg");
//        material.setMiniumTemp(20);
//        material.setDescription("askdh");
//        material.setActive(false);
//        material.setName("ashdif");
//        material.setCode("asbdk");
//        material.setPreCode("iahsdf");
//        material.setEncapsulation("halidfh");
//        material.setMaxTemp(90);
//        material.setRemark("hiash");
//        Type type=new Type();
//        type.setTypeName("hajisd");
//        material.setType(type);
//        materialRepository.save(material);
    }

    @Test
    public void deleteMaterial() throws Exception {
    }

    @Test
    public void modifyMaterial() throws Exception {
    }

    @Test
    public void findMaterial() throws Exception {
        String ty="有源";
        Type type=typeRepository.findByTypeName(ty);
      List<Material>materials=materialRepository.find(type,null,null,null);
        String s="";
    }
    @Test
    public void findMaterialCode() throws Exception {
        String ty="有源";
        List<String>codes=materialRepository.findMaterialCode();
        String s="";
    }
    @Test
    public void findMaterialModelNumber() throws Exception {
        String ty="有源";
        List<String>modelNumbers=materialRepository.findMaterialModelNumber();
        String s="";
    }
}