package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Models.FindStorageModel;
import com.atw.bpmsystem.Models.MaterialModel;
import com.atw.bpmsystem.Repositories.MaterialRepository;
import com.atw.bpmsystem.Services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRepository storageRepository;

    /**添加物料
     * 1.传入参数界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的添加操作
     * @param materialModel
     * @return
     */
    @RequestMapping(value = "/api/addMaterial", method = RequestMethod.POST,consumes = "application/json")
     // @PreAuthorize("hasRole('STOREHOUSE')AND hasRole('BUYER')")
    @ResponseBody
    public Map<String, Object> addMaterial(@RequestBody MaterialModel materialModel)
    {
        return materialService.addMaterial(materialModel);
    }

    /**删除物料
     * 1.传入参数idList
     * 2.完成物料的删除操作
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteMaterialList", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> deleteMaterialList(@RequestBody List<Map<String,Integer>> idList)
    {
        return materialService.deleteMaterial(idList);
    }

    /**修改物料
     * 1.传入参数materialId以及界面上的所有物料字段（参考MaterialModel）
     * 2.完成物料的修改操作
     * @param materialModel
     * @return
     */
    @RequestMapping(value = "/api/modifyMaterial", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> modifyMaterial(@RequestBody MaterialModel materialModel)

    {
        return materialService.modifyMaterial(materialModel);
    }

    /**查询所有物料
     * 1.传入参数为空
     * 2.完成查询所有物料的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllMaterial", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllMaterial()
    {
        return materialService.findAllMaterial();
    }
    /**查询所有物料编码
     * 1.传入参数为空
     * 2.完成查询所有物料编码的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllMaterialCode", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllMaterialCode()
    {
        return materialService.findAllMaterialCode();
    }
    /**查询所有物料型号规格
     * 1.传入参数为空
     * 2.完成查询所有物料型号规格的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllMaterialModelNumber", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllMaterialModelNumber()
    {
        return materialService.findAllMaterialModelNumber();
    }
    /**查询所有物料封装
     * 1.传入参数为空
     * 2.完成查询所有物料封装的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllMaterialEncapsulation", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllMaterialEncapsulation()
    {
        return materialService.findAllMaterialEncapsulation();
    }
    /**通过条件查询所有物料
     * 1.传入参数为type, code,modelNumber, encapsulation
     * 2.完成通过条件查询所有物料的操作
     * @return
     */
    @RequestMapping(value = "/api/findMaterialByCondition", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> fingMaterial(@RequestBody FindStorageModel findStorageModel)
    {
        return materialService.findMaterial(findStorageModel);
    }
}
