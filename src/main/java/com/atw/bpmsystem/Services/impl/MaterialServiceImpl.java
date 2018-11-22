package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Entities.Type;
import com.atw.bpmsystem.Models.FindStorageModel;
import com.atw.bpmsystem.Models.MaterialModel;
import com.atw.bpmsystem.Repositories.MaterialRepository;
import com.atw.bpmsystem.Repositories.StorageRepository;
import com.atw.bpmsystem.Repositories.TypeRepository;
import com.atw.bpmsystem.Services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableTransactionManagement
public class MaterialServiceImpl implements MaterialService{
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private StorageRepository storageRepository;
    /**添加物料
     * 1.传入参数，界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的添加操作
     * @param materialModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addMaterial(MaterialModel materialModel) {
        Map<String, Object> modelMap=new HashMap<>();
        Material material=modelToMaterial(materialModel);
        if(!StringUtils.isEmpty(material)){
            //Material oldMaterial=materialRepository.findByPreCodeAndCode(material.getPreCode(),material.getCode());
            Material oldMaterial=materialRepository.findByNameAndModelNumberAndEncapsulation(material.getName(),material.getModelNumber(),material.getEncapsulation());
            if(StringUtils.isEmpty(oldMaterial)){
                materialRepository.save(material);
                modelMap.put("success",true);
                modelMap.put("Msg", "添加物料成功！");
                modelMap.put("Material", material);
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "当前物料的pricode和code在库存已经存在，请输入有区别的新物料！");
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "输入物料为空，请输入物料！");
        }
        return modelMap;
    }
    /**删除物料
     * 1.传入参数idList
     * 2.完成物料的删除操作
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteMaterial(List<Map<String, Integer>> idList) {
        Map<String, Object> modelMap=new HashMap<>();
            if(!idList.isEmpty()){
                for (Map<String, Integer> id:idList) {
                    if(materialRepository.existsById(id.get("id"))){
                        Material material=materialRepository.getOne(id.get("id"));
                        materialRepository.delete(material);
                        modelMap.put("success", true);
                        modelMap.put("Msg", "删除物料"+material+"成功！");
                    }else {
                        modelMap.put("success", false);
                        modelMap.put("Msg", "删除物料失败，请输入正确的id！");
                    }
                }
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "删除物料失败，请输入删除清单！");
            }
        return modelMap;
    }
    /**修改物料
     * 1.传入参数，materialId以及界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的修改操作
     * @param materialModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyMaterial(MaterialModel materialModel) {
        Map<String, Object> modelMap=new HashMap<>();
        Material material=modelToMaterial(materialModel);
        if(!StringUtils.isEmpty(material)){
            if(materialRepository.existsById(material.getMaterialId())){
                materialRepository.save(material);
                modelMap.put("success", true);
                modelMap.put("Msg", "修改物料成功！");
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "修改物料失败，请输入正确的id！");
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "输入物料为空，请输入物料！");
        }
        return modelMap;
    }
    /**查询所有物料
     * 1.传入参数为空
     * 2.完成查询所有物料的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllMaterial() {
        Map<String, Object> modelMap=new HashMap<>();
        List<MaterialModel> materialModels=new ArrayList<>();
           List<Material> materials=materialRepository.findAll();
           if(!materials.isEmpty()){
               for (Material material:materials) {
                   int amount=0;
                   float price=0;
                   MaterialModel materialModel=materialToModel(material);
                   List<Storage> storages=storageRepository.findByMaterial(material);
                   if(!storages.isEmpty()){
                       for (Storage storage:storages) {
                           amount=amount+storage.getAmount();
                           price=storage.getPrice();
                       }
                       materialModel.setAmount(amount);
                       materialModel.setPrice(price);
                   }else {
                       materialModel.setAmount(0);
                       materialModel.setPrice(0);
                   }
                   materialModels.add(materialModel);
               }
               modelMap.put("success", true);
               modelMap.put("Msg", "获取物料成功！");
               modelMap.put("materialModels",materialModels);
           }else {
               modelMap.put("success", false);
               modelMap.put("Msg", "物料清单为空！");
           }
        return modelMap;
    }
    /**查询所有物料编码
     * 1.传入参数为空
     * 2.完成查询所有物料编码的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllMaterialCode() {
        Map<String, Object>modelMap=new HashMap<>();
        List<String>codes=materialRepository.findMaterialCode();
        if(!codes.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","查找物料编码成功！");
            modelMap.put("codes",codes);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","物料编码为空，查找物料编码失败！");
        }
        return modelMap;
    }
    /**查询所有物料型号规格
     * 1.传入参数为空
     * 2.完成查询所有物料型号规格的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllMaterialModelNumber() {
        Map<String, Object>modelMap=new HashMap<>();
        List<String>modelNumbers=materialRepository.findMaterialModelNumber();
        if(!modelNumbers.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","查找物料型号规格成功！");
            modelMap.put("modelNumbers",modelNumbers);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","物料型号规格为空，查找物料型号规格失败！");
        }
        return modelMap;
    }
    /**查询所有物料封装
     * 1.传入参数为空
     * 2.完成查询所有物料封装的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllMaterialEncapsulation() {
        Map<String, Object>modelMap=new HashMap<>();
        List<String>encapsulations=materialRepository.findMaterialEncapsulation();
        if(!encapsulations.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","查找物料封装成功！");
            modelMap.put("encapsulations",encapsulations);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","物料封装为空，查找物料封装失败！");
        }
        return modelMap;
    }
    /**通过条件查询所有物料
     * 1.传入参数为type, code,modelNumber, encapsulation
     * 2.完成通过条件查询所有物料的操作
     * @return
     */
    @Override
    public Map<String, Object> findMaterial(FindStorageModel findStorageModel) {
        Map<String, Object> modelMap=new HashMap<>();
        int amount=0;
        float price=0;
        Type type1=typeRepository.findByTypeName(findStorageModel.getType());
        List<MaterialModel> materialModels=new ArrayList<>();
        List<Material> materials=materialRepository.find(type1,findStorageModel.getCode(),findStorageModel.getModelNumber(), findStorageModel.getEncapsulation());
        if(!materials.isEmpty()){
            for (Material material:materials) {
                MaterialModel materialModel=materialToModel(material);
                List<Storage> storages=storageRepository.findByMaterial(material);
                if(!storages.isEmpty()){
                    for (Storage storage:storages) {
                        amount=amount+storage.getAmount();
                        price=storage.getPrice();
                    }
                    materialModel.setAmount(amount);
                    materialModel.setPrice(price);
                }else {
                    materialModel.setAmount(0);
                    materialModel.setPrice(0);
                }
                materialModels.add(materialModel);
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "获取物料成功！");
            modelMap.put("materialModels",materialModels);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "物料清单为空！");
        }
        return modelMap;
    }
    /**materialModel转换为material
     * @param materialModel
     * @return
     */
    @Override
    public Material modelToMaterial(MaterialModel materialModel) {
        Material material=new Material();
            material.setMaterialId(materialModel.getMaterialId());
            material.setRemark(materialModel.getRemark());
            material.setName(materialModel.getName());
            material.setPreCode(materialModel.getPreCode());
            material.setCode(materialModel.getCode());
            material.setRoHS(materialModel.getRoHS());
            material.setActive(materialModel.getActive());
            material.setDescription(materialModel.getDescription());
            material.setModelNumber(materialModel.getModelNumber());
            material.setMiniumTemp(materialModel.getMiniumTemp());
            material.setMaxTemp(materialModel.getMaxTemp());
            material.setEncapsulation(materialModel.getEncapsulation());
            material.setShelfLife(materialModel.getShelfLife());
            if(materialModel.getTypeName()!=null&&materialModel.getTypeName()!=""){
                Type type=typeRepository.findByTypeName(materialModel.getTypeName());
                if(!StringUtils.isEmpty(type)) {
                    material.setType(type);
                }else {
                    Type type1=new Type();
                    type1.setTypeName(materialModel.getTypeName());
                    material.setType(type1);
                }
            }

        return material;
    }
    /**material转换为materialModel
     * @param material
     * @return
     */
    @Override
    public MaterialModel materialToModel(Material material) {
       MaterialModel materialModel=new MaterialModel();
       materialModel.setMaterialId(material.getMaterialId());
       materialModel.setRemark(material.getRemark());
       materialModel.setName(material.getName());
       materialModel.setPreCode(material.getPreCode());
       materialModel.setCode(material.getCode());
       materialModel.setRoHS(material.getRoHS());
       materialModel.setActive(material.getActive());
       materialModel.setDescription(material.getDescription());
       materialModel.setModelNumber(material.getModelNumber());
       materialModel.setMaxTemp(material.getMaxTemp());
       materialModel.setMiniumTemp(material.getMiniumTemp());
       materialModel.setEncapsulation(material.getEncapsulation());
       materialModel.setShelfLife(material.getShelfLife());
       if(material.getType()!=null)
       materialModel.setTypeName(material.getType().getTypeName());
       return materialModel;
    }
}
