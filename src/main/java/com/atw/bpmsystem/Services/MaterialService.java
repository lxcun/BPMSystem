package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Type;
import com.atw.bpmsystem.Models.FindStorageModel;
import com.atw.bpmsystem.Models.MaterialModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MaterialService {
    /**添加物料
     * 1.传入参数，界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的添加操作
     * @param materialModel
     * @return
     */
    public Map<String,Object> addMaterial(MaterialModel materialModel);
    /**删除物料
     * 1.传入参数idList
     * 2.完成物料的删除操作
     * @param idList
     * @return
     */
    public Map<String,Object> deleteMaterial(List<Map<String,Integer>> idList);

    /**修改物料
     * 1.传入参数，materialId以及界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的修改操作
     * @param materialModel
     * @return
     */
    public Map<String,Object> modifyMaterial(MaterialModel materialModel);

    /**查询所有物料
     * 1.传入参数为空
     * 2.完成查询所有物料的操作
     * @return
     */
    public Map<String,Object> findAllMaterial();
    /**查询所有物料编码
     * 1.传入参数为空
     * 2.完成查询所有物料编码的操作
     * @return
     */
    public Map<String,Object> findAllMaterialCode();
    /**查询所有物料型号规格
     * 1.传入参数为空
     * 2.完成查询所有物料型号规格的操作
     * @return
     */
    public Map<String,Object> findAllMaterialModelNumber();
    /**查询所有物料封装
     * 1.传入参数为空
     * 2.完成查询所有物料封装的操作
     * @return
     */
    public Map<String,Object> findAllMaterialEncapsulation();


    /**通过条件查询所有物料
     * 1.传入参数为type, code,modelNumber, encapsulation
     * 2.完成通过条件查询所有物料的操作
     * @return
     */
    public Map<String, Object> findMaterial(FindStorageModel findStorageModel);

    /**materialModel转换为material
     * @param materialModel
     * @return
     */
    public Material modelToMaterial(MaterialModel materialModel);

    /**material转换为materialModel
     * @param material
     * @return
     */
    public MaterialModel materialToModel(Material material);
}
