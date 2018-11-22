package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.FindStorageModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@EnableTransactionManagement
public class StorageServiceImpl implements StorageService {
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private TypeRepository typeRepository;
    /**添加库存
     * 1.传入参数，界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的添加操作
     * @param storage
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> addStorage(Storage storage) {
        Map<String,Object> modelMap=new HashMap<>();
        if(!StringUtils.isEmpty(storage)) {
            storageRepository.save(storage);
            modelMap.put("success", true);
            modelMap.put("Msg", "添加库存"+storage+"成功！");
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "输入库存为空，请输入库存！");
        }
        return modelMap;
    }
    /**删除库存
     * 1.传入参数idList
     * 2.完成库存的删除操作
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteStorageList(List<Map<String,Integer>> idList) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        for (Map<String,Integer> map:idList) {
            if (storageRepository.existsById(map.get("id"))) {
                Storage storage=storageRepository.getOne(map.get("id"));
                modelMap.put("success", true);
                modelMap.put("Msg", "删除库存物料成功");
                modelMap.put("Storage", storageRepository.getOne(map.get("id")));
                storageRepository.delete(storage);
            } else {
                modelMap.put("success", false);
                modelMap.put("Msg", "库存没有该物料，删除库存物料失败");
            }
        }
            return modelMap;
    }

    /**修改库存
     * 1.传入参数，storageId以及界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的修改操作
     * @param storage
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyStorage(Storage storage) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(storageRepository.existsById(storage.getStorageId())) {
            storageRepository.save(storage);
            modelMap.put("success",true);
            modelMap.put("modifyStorage",storage);
            modelMap.put("Msg","修改库存物料成功");
        }
        else {
                modelMap.put("success",false);
                modelMap.put("Msg","修改库存物料失败,传入storageId为0");
            }
            return modelMap;
    }

    /**查询所有库存
     * 1.传入参数为空
     * 2.完成查询所有库存的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllStorage() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<Storage> storages=storageRepository.findAll();
        if(!storages.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","获取库存物料列表成功");
            modelMap.put("listStorage",storages);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","库存数据为空，获取库存物料失败");
        }
        return modelMap;
    }

    /**通过条件查询所有库存
     * 1.传入参数为FindStorageModel
     * 2.完成查询所有库存的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllStorageByCondition(FindStorageModel findStorageModel){
        Map<String, Object> modelMap=new HashMap<>();
        Type type=typeRepository.findByTypeName(findStorageModel.getType());
        List<Integer> storageIds=storageRepository.findByCondition(type,findStorageModel.getCode(),
                findStorageModel.getModelNumber(),findStorageModel.getEncapsulation(),findStorageModel.getProgramNumber());
        if(!storageIds.isEmpty()){
            List<Storage> storages=new ArrayList<>();
            for (Integer storageId:storageIds) {
                Storage storage=storageRepository.getOne(storageId);
                storages.add(storage);
            }
            modelMap.put("success",true);
            modelMap.put("Msg","获取库存物料列表成功");
            modelMap.put("listStorage",storages);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","库存数据为空，获取库存物料失败");
        }
        return modelMap;
    }

    /**根据id查询库存
     * 1.传入参数为id
     * 2.完成通过id查询库存的操作
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findStorageById(Map<String,Integer> id){
    Map<String, Object> modelMap = new HashMap<String, Object>();
    if(storageRepository.existsById(id.get("id"))){
            Storage storage=storageRepository.getOne(id.get("id"));
             modelMap.put("success",true);
             modelMap.put("Msg","获取库存物料成功");
             modelMap.put("Storage",storage);
            }
    else {
            modelMap.put("success",false);
            modelMap.put("Msg","库存没有该物料，获取库存物料失败");
            }
    return modelMap;
    }

    /**通过materialId查找库存
     * @param materialId
     * @return
     */
    @Override
    public Map<String, Object> findStorageByMaterialId(Integer materialId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(materialRepository.existsById(materialId)){
            Material material=materialRepository.getOne(materialId);
            List<Storage> storages=storageRepository.findByMaterial(material);
            if(!storages.isEmpty()){
                modelMap.put("success",true);
                modelMap.put("Msg","获取库存物料成功");
                modelMap.put("Storages",storages);
            }else {
                modelMap.put("success",false);
                modelMap.put("Msg","库存没有该物料，获取库存物料失败");
            }
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","库存没有该物料，获取库存物料失败");
        }
        return modelMap;
    }
    /**查询所有库存表里面的项目编号
     * 1.传入参数为空
     * 2.完成查询所有库存表中有的项目编号的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllProgramNumber() {
        Map<String, Object>modelMap=new HashMap<>();
        List<String>programNumbers=storageRepository.findProgramNumber();
        if(!programNumbers.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","查找项目编号成功！");
            modelMap.put("programNumbers",programNumbers);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","项目编号为空，查找项目编号失败！");
        }
        return modelMap;
    }
}