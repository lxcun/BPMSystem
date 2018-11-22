package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Models.QualityAuditModel;
import com.atw.bpmsystem.Services.QualityAuditService;
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
public class QualityAuditController {
    @Autowired
    private QualityAuditService qualityAuditService;
    /**新建新的质检（质检员调用）
     * 1.传入参数为质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/addCheck", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addCheck(@RequestBody QualityAuditModel qualityAuditModel, Principal principal)
    {

        return qualityAuditService.addCheck(qualityAuditModel,principal);
    }
    /**提交质检（质检员调用）
     * 1.传入参数为qualityAuditId和质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/submitCheck", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> submitCheck(@RequestBody QualityAuditModel qualityAuditModel, Principal principal)
    {

        return qualityAuditService.submitCheck(qualityAuditModel,principal);
    }
    /**删除质检单
     * 1.传入参数idList
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteCheck",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object>deleteCheck(@RequestBody List<Map<String,Integer>> idList){
        return qualityAuditService.deleteCheck(idList);
    }
    @RequestMapping(value = "/api/modifyCheck",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object>modifyCheck(@RequestBody QualityAuditModel qualityAuditModel){
        return qualityAuditService.modifyCheck(qualityAuditModel);
    }
    /**查询所有质检清单
     * 1.传入参数为空
     * @return
     */
    @RequestMapping(value = "/api/findAllCheck", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllCheck()
    {
        return qualityAuditService.findAllCheck();
    }
    /**查询我保存但是未提交的所有质检（质检调用）
     * 1.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyAddCheck", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyAddCheck(Principal principal)
    {
        return qualityAuditService.findMyAddCheck(principal);
    }
    /**查询通过的所有的质检（入库人员调用）
     * 1.传入参数为空
     * @return
     */
    @RequestMapping(value = "/api/findCheckToInStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findCheckToInStorage()
    {
        return qualityAuditService.findCheckToInStorage();
    }


}
