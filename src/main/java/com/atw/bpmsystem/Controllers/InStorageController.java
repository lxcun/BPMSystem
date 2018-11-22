package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.InStorage;
import com.atw.bpmsystem.Models.InStorageModel;
import com.atw.bpmsystem.Models.ListInStorageModel;
import com.atw.bpmsystem.Services.InStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 3600)
@RestController

public class InStorageController {
    @Autowired
    private InStorageService inStorageService;

    /**
     * 添加入库单
     * 1.传入参数，界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成添加入库单的操作
     * @param inStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/addInStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> addInStorage(@RequestBody InStorageModel inStorageModel,Principal principal)
    {
        return inStorageService.addInStorage(inStorageModel,principal);
    }

    /**
     * 批量添加入库单
     * 1.传入参数，界面上所有的入库字段ListInStorageModel（参照ListInStorageModel和InStorageModel和DetailStorageModel）Lis格式
     * 2.完成批量添加入库单的操作
     * @param
     * @return
     */
    @RequestMapping(value = "/api/addInStorageList", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> addInStorageList(@RequestBody ListInStorageModel listInStorageModel,Principal principal)
    {
        return inStorageService.addInStorageList(listInStorageModel,principal);
    }

    /**修改入库单
     * 1.传入参数，inStorageId以及界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成修改入库单的操作
     * @param inStorageModel
     * @return
     */
    @RequestMapping(value = "/api/modifyInStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> modifyInStorage(@RequestBody InStorageModel inStorageModel)
    {
        return inStorageService.modifyInStorage(inStorageModel);
    }

    /**
     * 删除入库单
     * 1.传入参数，idList
     * 2.完成删除入库单的操作
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteInStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> deleteInStorage(@RequestBody List<Map<String,Integer>> idList)
    {
        return inStorageService.deleteInStorage(idList);
    }

    /**
     *
     * @param inStorageModel
     * @param principal
     * @return
     */
//    @RequestMapping(value = "/api/approvalInStorage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Map<String, Object> approvalInStorage(@RequestBody InStorageModel inStorageModel,Principal principal)
//    {
//        return inStorageService.approvalInStorage(inStorageModel,principal);
//    }

    /**查询所有入库单
     * 1.传入参数为空
     * 2.完成查询所有入库单的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllInStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingAllInStorage()
    {
        return inStorageService.findAllInStorage();
    }

    /**通过id查询入库单
     * 1.传入参数id
     * 2.完成通过id的值查询入库单的操作
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/findInStorageById", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> fingInStorageById(@RequestBody Map<String,Integer> id)
    {
        return inStorageService.findInStorageById(id.get("id"));
    }

}
