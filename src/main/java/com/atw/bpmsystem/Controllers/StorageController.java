package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Models.FindStorageModel;
import com.atw.bpmsystem.Repositories.StorageRepository;
import com.atw.bpmsystem.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 36000)
@RestController
public class StorageController{
    @Autowired
    private StorageService storageService;

    /**添加库存
     * 1.传入参数，界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的添加操作
     * @param storage
     * @return
     */
    @RequestMapping(value = "/api/addStorage", method = RequestMethod.POST,consumes = "application/json")
    @PreAuthorize("hasRole('STOREHOUSE')AND hasRole('BUYER')")
    @ResponseBody
    public Map<String, Object> addStorage(@RequestBody Storage storage)
    {
        return storageService.addStorage(storage);
    }

    /**删除库存
     * 1.传入参数idList
     * 2.完成库存的删除操作
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteStorageList", method = RequestMethod.POST,consumes = "application/json")
//    @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasRole('STOREHOUSE')")
    @ResponseBody
    public Map<String, Object> deleteStorageList(@RequestBody List<Map<String,Integer>> idList)
    {
        return storageService.deleteStorageList(idList);
    }

    /**修改库存
     * 1.传入参数，storageId以及界面上的所有库存字段（参考MaterialModel和Storage）
     * 2.完成库存的修改操作
     * @param storage
     * @return
     */
@RequestMapping(value = "/api/modifyStorage", method = RequestMethod.POST,consumes = "application/json")
@ResponseBody
public Map<String, Object> modifyStorage(@RequestBody Storage storage)

{
    return storageService.modifyStorage(storage);
}

    /**查询所有库存
     * 1.传入参数为空
     * 2.完成查询所有库存的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllStorage()

    {
        return storageService.findAllStorage();
    }

    /**根据id查询库存
     * 1.传入参数为id
     * 2.完成通过id查询库存的操作
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/findStorageById", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingStorageById(@RequestBody Map<String,Integer> id)
    {
        return storageService.findStorageById(id);
    }
    /**根据materialId查询库存
     * 1.传入参数为materialId
     * 2.完成通过materialId查询库存的操作
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/api/findStorageByMaterialId", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingStorageByMaterialId(Integer materialId)
    {
        return storageService.findStorageByMaterialId(materialId);
    }

    /**根据FindStorageModel查询库存
     * 1.传入参数为FindStorageModel
     * 2.完成通过FindStorageModel查询库存的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllStorageByCondition", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> findAllStorageByCondition(@RequestBody FindStorageModel findStorageModel)
    {
        return storageService.findAllStorageByCondition(findStorageModel);
    }
    /**查询所有库存表里面的项目编号
     * 1.传入参数为空
     * 2.完成查询所有库存表中有的项目编号的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllProgramNumber", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllProgramNumber()
    {
        return storageService.findAllProgramNumber();
    }

}
