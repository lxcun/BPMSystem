package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.ProgramManage;
import com.atw.bpmsystem.Entities.Seller;
import com.atw.bpmsystem.Repositories.ProgramManageRepository;
import com.atw.bpmsystem.Services.ProgramManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@EnableTransactionManagement
@Service
public class ProgramManageServiceImpl implements ProgramManageService {
    @Autowired
    private ProgramManageRepository programManageRepository;
    @Transactional
    @Override
    public Map<String, Object> addProgramManage(ProgramManage programManage) {
        Map<String,Object>modelMap=new HashMap<>();
        if(programManage!=null&&programManage.getProgramName()!=null){
            ProgramManage programManage1=programManageRepository.findByProgramNameAndAndProgramNumber(programManage.getProgramName(),programManage.getProgramNumber());
            if(programManage1==null){
                programManageRepository.save(programManage);
                modelMap.put("success",true);
                modelMap.put("Msg","添加项目信息成功");
            }else {
                modelMap.put("success",false);
                modelMap.put("Msg","传入的项目信息已经存在，添加失败");
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","传入的项目信息为空，添加失败");
        }
        return modelMap;
    }
    @Transactional
    @Override
    public Map<String, Object> deleteProgramManage(List<Map<String, Integer>> idList) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!idList.isEmpty()){
            for (Map<String,Integer> id:idList) {
                ProgramManage programManage= programManageRepository.getOne(id.get("id"));
                if(programManageRepository.findByProgramNameAndAndProgramNumber(programManage.getProgramName(),programManage.getProgramNumber())!=null||!(StringUtils.isEmpty(programManageRepository.findByProgramNameAndAndProgramNumber(programManage.getProgramName(),programManage.getProgramNumber())))){
                    programManageRepository.delete(programManage);
                    modelMap.put("success",true);
                    modelMap.put("Msg","删除项目信息成功");
                }
                else {
                    modelMap.put("success",false);
                    modelMap.put("Msg","项目名和项目编号为空，删除失败");
                }
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","输入id为空，请输入id");
        }
        return modelMap;
    }
    @Transactional
    @Override
    public Map<String, Object> modifyProgramManage(ProgramManage programManage) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!StringUtils.isEmpty(programManage.getProgramNumberId())||programManage.getProgramName()!=null){
            programManageRepository.save(programManage);
            modelMap.put("success",true);
            modelMap.put("Msg","销售商信息修改成功");
            modelMap.put("ProgramMagage",programManage);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","项目信息或者id为空，请输入项目信息！");
        }
        return modelMap;
    }

    @Override
    public Map<String, Object> findAllProgramManage() {
        Map<String, Object> modelMap=new HashMap<>();
        List<ProgramManage> programManages=programManageRepository.findAll();
        if(!programManages.isEmpty()){
            modelMap.put("success",true);
            modelMap.put("Msg","项目信息获取成功");
            modelMap.put("ProgramManages",programManages);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","没有项目信息！");
        }
        return modelMap;
    }
}
