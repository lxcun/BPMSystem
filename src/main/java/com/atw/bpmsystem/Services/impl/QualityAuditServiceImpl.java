package com.atw.bpmsystem.Services.impl;

import com.alibaba.fastjson.JSON;
import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.DetailQualityAuditModel;
import com.atw.bpmsystem.Models.QualityAuditModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.MailService;
import com.atw.bpmsystem.Services.QualityAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@EnableTransactionManagement
public class QualityAuditServiceImpl implements QualityAuditService{
     @Autowired
     private QualityAuditRepository qualityAuditRepository;
     @Autowired
     private PurchaseRepository purchaseRepository;
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private DetailPurchaseRepository detailPurchaseRepository;
     @Autowired
     private RoleRepository roleRepository;
     @Autowired
     private MailService mailService;
     @Autowired
     private DetailQualityAuditRepository detailQualityAuditRepository;
    /**新建新的质检（质检员调用）
     * 1.传入参数为质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
     @Transactional
     @Override
    public Map<String, Object> addCheck(QualityAuditModel qualityAuditModel, Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
         QualityAudit qualityAudit=modelToQualityAudit(qualityAuditModel);
        if(!StringUtils.isEmpty(qualityAudit)){
            qualityAudit.setState(0);
            qualityAuditRepository.save(qualityAudit);
            List<DetailQualityAudit> detailQualityAudits=qualityAudit.getDetailQualityAudits();
            if(!detailQualityAudits.isEmpty()){
                for (DetailQualityAudit detailQualityAudit:detailQualityAudits
                     ) {
                    detailQualityAudit.setQualityAudit(qualityAudit);
                }
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "添加质检单成功");
            modelMap.put("qualityAudit", qualityAudit);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "质检记录为空，添加记录失败");
        }
        return modelMap;
    }
    /**提交质检（质检员调用）
     * 1.传入参数为qualityAuditId和质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> submitCheck(QualityAuditModel qualityAuditModel, Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
            QualityAudit qualityAudit=modelToQualityAudit(qualityAuditModel);
            LocalDate endDate=null;
            Integer shelfLife=null;
        String temp_str="";
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        temp_str=sdf.format(dt);
        if(!StringUtils.isEmpty(qualityAudit)){
            List<DetailQualityAudit>detailQualityAudits=qualityAudit.getDetailQualityAudits();
            List<DetailQualityAudit> detailQualityAudits1=detailQualityAuditRepository.findByBatchNumberLike("QA"+temp_str+"%");
           int i=detailQualityAudits1.size()+1;
            User qa=userRepository.findByLoginName(principal.getName());
            log.info(qa+"提交了质检清单"+ JSON.toJSONString(qualityAuditModel));
            qualityAudit.setQa(qa);
            qualityAudit.setAuditDate(LocalDate.now());
            qualityAudit.setState(1);
            qualityAuditRepository.save(qualityAudit);
            for (DetailQualityAudit detailQualityAudit:detailQualityAudits) {

                Format f1=new DecimalFormat("000");
                String f2=f1.format(i);
                String batchNumber="QA"+temp_str+""+f2;
                i=i+1;
                detailQualityAudit.setQualityAudit(qualityAudit);
                detailQualityAudit.setBatchNumber(batchNumber);
                if(detailQualityAudit.getDetailPurchase()!=null){
                    Purchase purchase=detailQualityAudit.getDetailPurchase().getPurchase();
                    if(qualityAudit.getResult()) {
                        purchase.setExeState(7);
                    }else {
                        purchase.setExeState(6);
                    }
                    purchase.setQualityAudit(qualityAudit);
                    purchase.setQADate(LocalDate.now());
                    purchaseRepository.save(purchase);
                    endDate=detailQualityAudit.getProductionDate();
                    Material material=detailQualityAudit.getDetailPurchase().getMaterial();
                    if(material!=null)
                         shelfLife=material.getShelfLife();
                    if(endDate!=null&&shelfLife!=null)
                        endDate=endDate.plusMonths(shelfLife);
                    detailQualityAudit.setEndDate(endDate);
                }
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "提交质检单成功");
            modelMap.put("qualityAudit", qualityAudit);
            List<Role> roles=new ArrayList<>();
            List<User> examiners =new ArrayList<>();
            Role role=roleRepository.findByRole("ROLE_STOREHOUSE");
            roles.add(role);
            if(!roles.isEmpty()) {
                examiners = userRepository.findByRoles( roles);
            }
            String title="物料质检完成！";
            String text="物料质检完成，可以进行入库操作了！";
            if(!examiners.isEmpty()) {
                for (User examiner : examiners) {
                    if (examiner.getEmail() != null) {
                        mailService.sendEmail(examiner.getEmail(), title, text);
                    }
                }
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "质检记录为空，提交记录失败");
        }
        return modelMap;
    }
    /**删除质检单
     * 1.传入参数idList
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteCheck(List<Map<String, Integer>> idList) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!idList.isEmpty()){
            for (Map<String,Integer> id:idList) {
               QualityAudit qualityAudit=qualityAuditRepository.getOne(id.get("id"));
                if(qualityAudit.getQualityAuditId()!=null||!(StringUtils.isEmpty(qualityAudit))){
                    qualityAuditRepository.delete(qualityAudit);
                    modelMap.put("success",true);
                    modelMap.put("Msg","删除质检单成功");
                }
                else {
                    modelMap.put("success",false);
                    modelMap.put("Msg","质检单不存在，删除失败");
                }
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","输入id为空，请输入id");
        }
        return modelMap;
    }
    /**修改质检单
     * 1.传入参数为qualityAuditId和质检界面上的所有字段参照QualityAuditModel
     * @param qualityAuditModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyCheck(QualityAuditModel qualityAuditModel) {
        Map<String, Object> modelMap=new HashMap<>();
        if(qualityAuditModel!=null&&!StringUtils.isEmpty(qualityAuditModel)){
            QualityAudit qualityAudit=modelToQualityAudit(qualityAuditModel);
            if(qualityAuditModel.getQualityAuditId()!=null||qualityAuditRepository.existsById(qualityAuditModel.getQualityAuditId())){
                qualityAuditRepository.save(qualityAudit);
                modelMap.put("success",true);
                modelMap.put("Msg","质检单修改成功");
                modelMap.put("qualityAudit",qualityAudit);
            }else {
                modelMap.put("success",false);
                modelMap.put("Msg","质检单id为空，请输入质检单！");
            }

        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","质检单为空，请输入质检单！");
        }
        return modelMap;
    }
    /**查询所有质检清单
     * 1.传入参数为空
     * @return
     */
    @Override
    public Map<String, Object> findAllCheck() {
        Map<String, Object> modelMap=new HashMap<>();
            List<QualityAudit> qualityAudits=qualityAuditRepository.findAll();
            List<QualityAuditModel> qualityAuditModels=new ArrayList<>();
            if(!qualityAudits.isEmpty()){
                for (QualityAudit qualityAudit:qualityAudits) {
                    QualityAuditModel qualityAuditModel=qualityAuditToModel(qualityAudit);
                    qualityAuditModels.add(qualityAuditModel);
                }
                modelMap.put("success", true);
                modelMap.put("Msg", "获取质检列表成功");
                modelMap.put("qualityAuditModels", qualityAuditModels);
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "质检记录为空，获取质检记录失败");
            }
        return modelMap;
    }

    /**查询我保存但是未提交的所有质检（质检调用）
     * 1.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyAddCheck(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<QualityAudit> qualityAudits=qualityAuditRepository.findByQaAndState(userRepository.findByLoginName(principal.getName()),0);
        List<QualityAuditModel> qualityAuditModels=new ArrayList<>();
        if(!qualityAudits.isEmpty()){
            for (QualityAudit qualityAudit:qualityAudits) {
                QualityAuditModel qualityAuditModel=qualityAuditToModel(qualityAudit);
                qualityAuditModels.add(qualityAuditModel);
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "获取我保存的质检列表成功");
            modelMap.put("qualityAuditModels", qualityAuditModels);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "质检我保存的记录为空，获取保存的质检记录失败");
        }
        return modelMap;
    }
    /**查询通过的所有的质检（入库人员调用）
     * 1.传入参数为空
     * @return
     */
    @Override
    public Map<String, Object> findCheckToInStorage() {
        Map<String, Object> modelMap=new HashMap<>();
        List<QualityAudit> qualityAudits=qualityAuditRepository.findByResultAndState(true,1);
        List<QualityAuditModel> qualityAuditModels=new ArrayList<>();
        if(!qualityAudits.isEmpty()){
            for (QualityAudit qualityAudit:qualityAudits) {
                QualityAuditModel qualityAuditModel=qualityAuditToModel(qualityAudit);
                qualityAuditModels.add(qualityAuditModel);
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "获取通过的质检列表成功");
            modelMap.put("qualityAuditModels", qualityAuditModels);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "通过的质检记录为空，获取通过的质检记录失败");
        }
        return modelMap;
    }
    /**将model转换为QualityAudit
     * @param qualityAuditModel
     * @return
     */
    @Override
    public QualityAudit modelToQualityAudit(QualityAuditModel qualityAuditModel) {
        QualityAudit qualityAudit=new QualityAudit();
        if(qualityAuditModel.getQualityAuditId()!=null)
        qualityAudit.setQualityAuditId(qualityAuditModel.getQualityAuditId());
        qualityAudit.setName(qualityAuditModel.getName());
        qualityAudit.setRemark(qualityAuditModel.getRemark());
        qualityAudit.setResult(qualityAuditModel.getResult());
        qualityAudit.setState(qualityAuditModel.getState());
        List<DetailQualityAudit>detailQualityAudits=new ArrayList<>();
        List<DetailQualityAuditModel>detailQualityAuditModels=qualityAuditModel.getDetailQualityAuditModels();
        if(!detailQualityAuditModels.isEmpty()){
            for (DetailQualityAuditModel detailQualityAuditModel:detailQualityAuditModels) {
                DetailQualityAudit detailQualityAudit=new DetailQualityAudit();
                detailQualityAudit.setSampleCount(detailQualityAuditModel.getSampleCount());
                detailQualityAudit.setPassCount(detailQualityAuditModel.getPassCount());
                detailQualityAudit.setInStorageCount(detailQualityAuditModel.getInStorageCount());
                detailQualityAudit.setBatchNumber(detailQualityAuditModel.getBatchNumber());
                detailQualityAudit.setProductionDate(detailQualityAuditModel.getProductionDate());

                Float passRate=0.0f;
                if(detailQualityAuditModel.getPassCount()!=null&&detailQualityAuditModel.getSampleCount()!=0&&detailQualityAuditModel.getSampleCount()!=null)
                    passRate=detailQualityAuditModel.getPassCount().floatValue()/detailQualityAuditModel.getSampleCount();
                detailQualityAudit.setPassRate(passRate);
                detailQualityAudit.setDetailPurchase(detailPurchaseRepository.getOne(detailQualityAuditModel.getDetailPurchaseId()));
                detailQualityAudits.add(detailQualityAudit);
            }
        }
        qualityAudit.setDetailQualityAudits(detailQualityAudits);
        return qualityAudit;
    }

    /**QualityAudit转换为QualityAuditModel
     * @param qualityAudit
     * @return
     */
    @Override
    public QualityAuditModel qualityAuditToModel(QualityAudit qualityAudit) {
        QualityAuditModel qualityAuditModel=new QualityAuditModel();
        int inStorageCount=0;
        float inStoragePrice=0;
        qualityAuditModel.setQualityAuditId(qualityAudit.getQualityAuditId());
        qualityAuditModel.setName(qualityAudit.getName());
        qualityAuditModel.setRemark(qualityAudit.getRemark());
        qualityAuditModel.setResult(qualityAudit.getResult());
        qualityAuditModel.setState(qualityAudit.getState());
        if(qualityAudit.getQa()!=null)
        qualityAuditModel.setQaName(qualityAudit.getQa().getLoginName());
        List<DetailQualityAuditModel>detailQualityAuditModels=new ArrayList<>();
        List<DetailQualityAudit>detailQualityAudits=qualityAudit.getDetailQualityAudits();
        if(!StringUtils.isEmpty(detailQualityAudits)){
            for (DetailQualityAudit detailQualityAudit:detailQualityAudits) {
                DetailQualityAuditModel detailQualityAuditModel=new DetailQualityAuditModel();
                detailQualityAuditModel.setSampleCount(detailQualityAudit.getSampleCount());
                detailQualityAuditModel.setPassCount(detailQualityAudit.getPassCount());
                detailQualityAuditModel.setPassRate(detailQualityAudit.getPassRate());
                detailQualityAuditModel.setInStorageCount(detailQualityAudit.getInStorageCount());
                detailQualityAuditModel.setBatchNumber(detailQualityAudit.getBatchNumber());
                detailQualityAuditModel.setEndDate(detailQualityAudit.getEndDate());
                detailQualityAuditModel.setProductionDate(detailQualityAudit.getProductionDate());
                if(detailQualityAudit!=null&&detailQualityAudit.getInStorageCount()!=null)
                inStorageCount=inStorageCount+detailQualityAudit.getInStorageCount();
                DetailPurchase detailPurchase=detailQualityAudit.getDetailPurchase();
                if(!StringUtils.isEmpty(detailPurchase)){
                    if(detailQualityAudit.getInStorageCount()!=null&&detailPurchase.getPurchasePrice()!=null)
                    inStoragePrice=inStoragePrice+detailQualityAudit.getInStorageCount()*detailPurchase.getPurchasePrice();
                    detailQualityAuditModel.setDetailPurchaseId(detailPurchase.getDetailPurchaseId());
                    if(detailPurchase.getMaterial()!=null) {
                        detailQualityAuditModel.setCode(detailPurchase.getMaterial().getCode());
                        detailQualityAuditModel.setName(detailPurchase.getMaterial().getName());
                        detailQualityAuditModel.setMaterialId(detailPurchase.getMaterial().getMaterialId());
                        detailQualityAuditModel.setEncapsulation(detailPurchase.getMaterial().getEncapsulation());
                    }
                    detailQualityAuditModel.setRemark(detailPurchase.getRemark());
                    detailQualityAuditModel.setContractNumber(detailPurchase.getContractNumber());
                    detailQualityAuditModel.setExpirationDate(detailPurchase.getExpirationDate());
                    detailQualityAuditModel.setPurchaseModel(detailPurchase.getPurchaseModel());
                    if(detailPurchase.getSeller()!=null)
                    detailQualityAuditModel.setSellerName(detailPurchase.getSeller().getSellerName());
                    if(detailPurchase.getPurchaseCount()!=null)
                    detailQualityAuditModel.setPurchaseCount(detailPurchase.getPurchaseCount());
                    if(detailPurchase.getPurchasePrice()!=null)
                    detailQualityAuditModel.setPurchasePrice(detailPurchase.getPurchasePrice());
                    detailQualityAuditModel.setProgramNumber(detailPurchase.getProgramNumber());
                    detailQualityAuditModel.setOrderId(detailPurchase.getOrderId());
                    Purchase purchase=detailPurchase.getPurchase();
                    if(purchase!=null) {
                        qualityAuditModel.setPurchaseId(purchase.getPurchaseId());
                        qualityAuditModel.setOrderDate(purchase.getOrderDate());
                        qualityAuditModel.setReceivingDate(purchase.getReceivingDate());
                        if (purchase.getRequestor() != null)
                            qualityAuditModel.setRequestor(purchase.getRequestor().getLoginName());
                    }

                }
                detailQualityAuditModels.add(detailQualityAuditModel);
            }
        }
        qualityAuditModel.setInStorageCount(inStorageCount);
        qualityAuditModel.setInStoragePrice(inStoragePrice);
        qualityAuditModel.setDetailQualityAuditModels(detailQualityAuditModels);
        return qualityAuditModel;
    }
}
