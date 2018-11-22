package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.QualityAudit;
import com.atw.bpmsystem.Models.QualityAuditModel;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
@Service
public interface QualityAuditService {
    /**新建新的质检（质检员调用）
     * 1.传入参数为质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
    public Map<String,Object> addCheck(QualityAuditModel qualityAuditModel, Principal principal);

    /**提交质检（质检员调用）
     * 1.传入参数为qualityAuditId和质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
    public Map<String, Object> submitCheck(QualityAuditModel qualityAuditModel, Principal principal);

    /**删除质检单
     * 1.传入参数idList
     * @param idList
     * @return
     */
    public Map<String, Object> deleteCheck(List<Map<String,Integer>> idList);

    /**修改质检单
     * 1.传入参数为qualityAuditId和质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @return
     */
    public Map<String, Object> modifyCheck(QualityAuditModel qualityAuditModel);
    /**查询所有质检清单
     * 1.传入参数为空
     * @return
     */
    public Map<String,Object> findAllCheck();

    /**查询我保存但是未提交的所有质检（质检调用）
     * 1.传入参数为空
     * @param principal
     * @return
     */
    public Map<String,Object> findMyAddCheck(Principal principal);

    /**查询通过的所有的质检（入库人员调用）
     * 1.传入参数为空
     * @return
     */
    public Map<String,Object> findCheckToInStorage();

    /**将model转换为QualityAudit
     * @param qualityAuditModel
     * @return
     */
    public QualityAudit modelToQualityAudit(QualityAuditModel qualityAuditModel);

    /**QualityAudit转换为QualityAuditModel
     * @param qualityAudit
     * @return
     */
    public QualityAuditModel qualityAuditToModel(QualityAudit qualityAudit);

}
