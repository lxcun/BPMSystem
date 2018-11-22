package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.ProgramManage;
import com.atw.bpmsystem.Services.ProgramManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 3600)
@RestController
public class ProgramManageController {
    @Autowired
    private ProgramManageService programManageService;


    /**添加项目信息
     * 1.传入参数，界面上的所有项目信息字段（参考ProgramManage）
     * 2.完成项目信息的添加操作
     * @param
     * @return
     */
    @RequestMapping(value = "/api/addProgramManage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addProgramManage(@RequestBody ProgramManage programManage){
        return programManageService.addProgramManage(programManage);
    }

    /**删除项目信息
     * 1.传入参数idList
     * 2.完成项目信息的删除操作
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteProgramManage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> deleteProgramManage(@RequestBody List<Map<String,Integer>> idList){
       return programManageService.deleteProgramManage(idList);
    }

    /**修改项目信息
     * 1.传入参数，programManageId以及界面上的所有项目信息字段（参考ProgramManage）
     * 2.完成项目信息的修改操作
     * @param programManage
     * @return
     */
    @RequestMapping(value = "/api/modifyProgramManage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> modifyProgramManage(@RequestBody ProgramManage programManage){
        return programManageService.modifyProgramManage(programManage);
    }

    /**查询所有项目信息
     * 1.传入参数为空
     * 2.完成查询所有项目信息的操作
     * @return
     */
    @RequestMapping(value = "/api/findAllProgramManage",  method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllProgramManage(){
        return programManageService.findAllProgramManage();
    }
}
