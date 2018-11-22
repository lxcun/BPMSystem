package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.ProgramManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;

@Service
public interface ProgramManageService {

    /**添加项目信息
     * 1.传入参数，界面上的所有项目信息字段（参考ProgramManage）
     * 2.完成项目信息的添加操作
     * @param
     * @return
     */
    public Map<String, Object> addProgramManage(ProgramManage programManage);

    /**删除项目信息
     * 1.传入参数idList
     * 2.完成项目信息的删除操作
     * @param idList
     * @return
     */
    public Map<String, Object> deleteProgramManage(List<Map<String,Integer>> idList);

    /**修改项目信息
     * 1.传入参数，programManageId以及界面上的所有项目信息字段（参考ProgramManage）
     * 2.完成项目信息的修改操作
     * @param programManage
     * @return
     */
    public Map<String, Object> modifyProgramManage(ProgramManage programManage);

    /**查询所有项目信息
     * 1.传入参数为空
     * 2.完成查询所有项目信息的操作
     * @return
     */
    public Map<String, Object> findAllProgramManage();
}
