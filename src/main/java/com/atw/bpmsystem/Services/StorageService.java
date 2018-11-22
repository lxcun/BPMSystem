package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Entities.Type;
import com.atw.bpmsystem.Models.FindStorageModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StorageService {

    /**添加库存
     * 1.传入参数，界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的添加操作
     * @param storage
     * @return
     */
    public Map<String, Object> addStorage(Storage storage);

    /**删除库存
     * 1.传入参数idList
     * 2.完成库存的删除操作
     * @param idList
     * @return
     */
    public Map<String, Object> deleteStorageList(List<Map<String, Integer>> idList);

    /**修改库存
     * 1.传入参数，storageId以及界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的修改操作
     * @param storage
     * @return
     */
    public Map<String, Object> modifyStorage(Storage storage);

    /**查询所有库存
     * 1.传入参数为空
     * 2.完成查询所有库存的操作
     * @return
     */
    public Map<String, Object> findAllStorage();

    /**通过条件查询所有库存
     * 1.传入参数为FindStorageModel
     * 2.完成查询所有库存的操作
     * @return
     */
    public Map<String, Object> findAllStorageByCondition(FindStorageModel findStorageModel);

    /**根据id查询库存
     * 1.传入参数为id
     * 2.完成通过id查询库存的操作
     * @param id
     * @return
     */
    public Map<String, Object> findStorageById(Map<String,Integer> id);

    /**通过materialId查找库存
     * @param materialId
     * @return
     */
    public Map<String,Object>findStorageByMaterialId(Integer materialId);
    /**查询所有库存表里面的项目编号
     * 1.传入参数为空
     * 2.完成查询所有库存表中有的项目编号的操作
     * @return
     */
    public Map<String,Object> findAllProgramNumber();
}
