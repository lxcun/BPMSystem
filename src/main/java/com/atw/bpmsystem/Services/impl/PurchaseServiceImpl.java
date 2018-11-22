package com.atw.bpmsystem.Services.impl;

import com.alibaba.fastjson.JSON;
import com.atw.bpmsystem.Common.CreateView;
import com.atw.bpmsystem.Controllers.PurchaseController;
import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.DetailPurchaseModel;
import com.atw.bpmsystem.Models.FindPurchaseModel;
import com.atw.bpmsystem.Models.PurchaseModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.MailService;
import com.atw.bpmsystem.Services.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;


@EnableTransactionManagement
@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService{
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private OutStorageRepository outStorageRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DetailOutStorageRepository detailOutStorageRepository;
    @Autowired
    private DetailPurchaseRepository detailPurchaseRepository;
    @PersistenceContext
    private EntityManager em;
    /**
     * 新建采购申请（由采购人员新建采购申请）
     * 需要传入申请采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面查看
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addPurchase(PurchaseModel purchaseModel, Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
          Purchase purchase=modelToPurchase(purchaseModel);
        User user=userRepository.findByLoginName(principal.getName());
          if(!StringUtils.isEmpty(purchase)){

              log.info(user.getLoginName()+"新建采购单成功"+ JSON.toJSONString(purchaseModel));
              purchase.setRequestor(user);
              purchase.setCreatDate(LocalDate.now());
              purchaseRepository.save(purchase);
              modelMap.put("success",false);
              modelMap.put("Msg","添加采购清单成功！");
              modelMap.put("Purchase",purchase);
          }else {
              log.error(user.getLoginName()+"传入采购单为空，新建采购单失败！");
              modelMap.put("success",false);
              modelMap.put("Msg","采购申请为空，请输入采购申请！");
          }
        return modelMap;
    }
    /**
     * 提交采购申请（由采购人员提交采购申请）
     * 需要传入采购申请的purchaseId以及采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面
     * 格式和新建时相同
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> savePurchase(PurchaseModel purchaseModel, Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        String title="提交了采购申请";
        String text="提交了采购申请，希望部门经理可以审批一下，谢谢！";
        User user=userRepository.findByLoginName(principal.getName());
        Purchase purchase=modelToPurchase(purchaseModel);
        log.info(user.getLoginName()+"提交采购单"+ JSON.toJSONString(purchaseModel));
        if(!StringUtils.isEmpty(purchase)){
                purchase.setExeState(1);
                purchase.setRequestor(user);
                purchase.setSubmitDate(LocalDate.now());
            purchaseRepository.save(purchase);
            List<OutStorage>outStorages=purchase.getOutStorages();
            for (OutStorage outStorage:outStorages) {
                outStorage.setExeStat(5);
                outStorage.setPurchase(purchase);
                outStorageRepository.save(outStorage);
            }
                List<DetailPurchase>detailPurchases=purchase.getDetailPurchases();
                if(!detailPurchases.isEmpty()){
                    for (DetailPurchase detailPurchase:detailPurchases) {
                        detailPurchase.setPurchase(purchase);
                    }
                }
            List<Role> roles=new ArrayList<>();
            List<User> examiners =new ArrayList<>();
            Role role=roleRepository.findByRole("ROLE_DIVISIONMANAGER");
            roles.add(role);
            if(!roles.isEmpty()) {
                examiners = userRepository.findByOperationAndRoles("财务部", roles);
            }
                if(!examiners.isEmpty()) {
                    text = user.getLoginName() + text ;
                    for (User examiner:examiners) {
                        mailService.sendEmail(examiner.getEmail(),title,text);
                    }
                }else {
                     text= "没有找到财务身份的人，请电话通知一下！";
                    mailService.sendEmail(user.getEmail(),title,text);
                }

                modelMap.put("success",false);
                modelMap.put("Msg","提交采购清单成功！");
                modelMap.put("Purchase",purchase);

        }else {
            log.info(user.getLoginName()+"提交采购单失败");
            modelMap.put("success",false);
            modelMap.put("Msg","采购申请为空，请输入采购申请！");
        }
        return modelMap;
    }
    /**
     * 删除采购申请（操作人待定）
     * 传入参数为purchaseId的清单
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deletePurchase(List<Map<String,Integer>> idList) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!idList.isEmpty()){
            for (Map<String,Integer> id:idList) {
                if(purchaseRepository.existsById(id.get("id"))){
                    purchaseRepository.deleteById(id.get("id"));
                    modelMap.put("success",false);
                    modelMap.put("Msg","删除采购清单成功！");
                    modelMap.put("Purchase",purchaseRepository.getOne(id.get("id")));
                }else {
                    modelMap.put("success",false);
                    modelMap.put("Msg","id错误，删除采购清单失败！");
                }
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","id为空，删除采购清单失败！");
        }
        return modelMap;
    }
    /**
     * 修改采购申请（申请人操作）
     * 1.当选择保存和需要修改时调用
     * 2.传入参数为purchaseId和其他purchase所有字段
     * @param purchaseModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyPurchase(PurchaseModel purchaseModel) {
        Map<String, Object> modelMap=new HashMap<>();
        Purchase purchase=modelToPurchase(purchaseModel);
           if(!StringUtils.isEmpty(purchase)){
               if(purchaseRepository.existsById(purchase.getPurchaseId())){
                   purchaseRepository.save(purchase);
                   modelMap.put("success",false);
                   modelMap.put("Msg","修改采购清单成功！");
                   modelMap.put("Purchase",purchase);
               }else {
                   modelMap.put("success",false);
                   modelMap.put("Msg","传入采购的采购申请的id为空，修改采购清单失败");
               }
           }else {
               modelMap.put("success",false);
               modelMap.put("Msg","采购记录为空，修改采购清单失败");
           }
        return modelMap;
    }
    /**
     * 部门经理审批（财务调用）
     * 1.当进行审核时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> divisionManagerApprovalPurchase(PurchaseModel purchaseModel,Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        String title="审核了采购申请";
        String text="审核了采购申请，希望高管可以审批一下，谢谢！";
           if(!StringUtils.isEmpty(purchaseModel)){
               if(purchaseRepository.existsById(purchaseModel.getPurchaseId())){
                   Purchase purchase=purchaseRepository.getOne(purchaseModel.getPurchaseId());
                   User auditor=userRepository.findByLoginName(principal.getName());
                   User requestor=userRepository.findByLoginName(purchase.getRequestor().getLoginName());
                   purchase.setExeState(purchaseModel.getExeState());
                   purchase.setAuditAdvice(purchaseModel.getAuditAdvice());
                   purchase.setAuditor(auditor);
                   purchase.setAuditDate(LocalDate.now());
                   purchaseRepository.save(purchase);
                   switch (purchaseModel.getExeState()){
//                       case 0: {
//                           modelMap.put("success",false);
//                           modelMap.put("Msg","完成采购审核操作成功！");
//                           modelMap.put("purchase",purchase);
//                            title="采购申请需要修改";
//                            text="提交的采购申请未通过，需要修改，请看审核意见！";
//                           if(requestor!=null&&requestor.getEmail()!=null)
//                            mailService.sendEmail(requestor.getEmail(),title,text);
//
//                       }break;
                       case 2: {
                           User approver=userRepository.findByLoginName(purchaseModel.getApprover());
                           log.info(auditor.getLoginName()+"审核采购单到上级处"+ JSON.toJSONString(purchaseModel));
                           modelMap.put("success",false);
                           modelMap.put("Msg","完成采购审核操作成功！");
                           modelMap.put("purchase",purchase);
                           purchase.setApprover(approver);
                           if(approver!=null&&approver.getEmail()!=null){
                               text = auditor.getLoginName() + text ;
                               mailService.sendEmail(approver.getEmail(),title,text);
                           }

                       }break;
                       case 3: {
                            title="审批了采购申请";
                            text="审批了采购申请，希望采购可以采购一下，谢谢！";
                           purchase.setApprover(auditor);
                           purchase.setApprovalTime(LocalDate.now());
                           purchase.setApprovalAdvice(purchaseModel.getAuditAdvice());
                           modelMap.put("success",false);
                           modelMap.put("Msg","完成采购审批操作成功！");
                           modelMap.put("purchase",purchase);
                           log.info(auditor.getLoginName()+"审批采购单成功"+ JSON.toJSONString(purchaseModel));
                           if(requestor!=null&&requestor.getEmail()!=null){
                               text = auditor.getLoginName() + text ;
                               mailService.sendEmail(requestor.getEmail(),title,text);
                           }

                       }break;
                       case 8: {
                           modelMap.put("success",false);
                           modelMap.put("Msg","完成采购审核操作失败！");
                           title="采购申请失败";
                           text="审核你提交的采购申请未通过，请看审核意见！";
                           log.info(auditor.getLoginName()+"审核拒绝采购单"+ JSON.toJSONString(purchaseModel));
                           if(requestor!=null&&requestor.getEmail()!=null){
                               text = auditor.getLoginName() + text ;
                               mailService.sendEmail(requestor.getEmail(),title,text);
                           }

                       } break;
                       default:{
                           log.error("传入采购单的审核状态不对");
                           modelMap.put("success",false);
                           modelMap.put("Msg","传入的审核状态为空或者不对，审核采购申请失败！");
                       }  break;
                   }
               }else {
                   log.error("传入审核单的id不对");
                   modelMap.put("success",false);
                   modelMap.put("Msg","传入的采购申请id为空，审核采购清单失败！");
               }
           }else {
               log.error("传入审核单为空");
               modelMap.put("success",false);
               modelMap.put("Msg","传入采购申请为空，审核采购清单失败！");
           }
        return modelMap;
    }
    /**
     * 高管审批（高管调用）
     * 1.当进行审批时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> seniorExecutiveApprovalPurchase(PurchaseModel purchaseModel,Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        String title="审批了采购申请";
        String text="审批了你的采购申请，希望采购可以采购一下，谢谢！";
        if(!StringUtils.isEmpty(purchaseModel)){
            if(purchaseRepository.existsById(purchaseModel.getPurchaseId())){
                Purchase purchase=purchaseRepository.getOne(purchaseModel.getPurchaseId());
                User approver=userRepository.findByLoginName(principal.getName());
                switch (purchaseModel.getExeState()){
//                    case 0: {
//                        modelMap.put("success",false);
//                        modelMap.put("Msg","完成采购审批操作成功！");
//                        modelMap.put("purchase",purchase);
//                        title="采购申请需要修改";
//                        text="提交的采购申请未通过，需要修改，请看审批意见！";
//                        mailService.sendEmail(requestor.getEmail(),title,text);
//
//                    }break;
                    case 3: {
                        log.info(approver.getLoginName()+"审批采购单为成功"+ JSON.toJSONString(purchaseModel));
                        modelMap.put("success",false);
                        modelMap.put("Msg","完成采购审批操作成功！");
                        modelMap.put("purchase",purchase);
                        User requestor=userRepository.findByLoginName(purchase.getRequestor().getLoginName());
                        if(requestor!=null&&requestor.getEmail()!=null){
                            text = approver.getLoginName() + text ;
                            mailService.sendEmail(requestor.getEmail(),title,text);
                        }
                    }break;
                    case 8: {
                        log.info(approver.getLoginName()+"审批采购单为拒绝"+ JSON.toJSONString(purchaseModel));
                        modelMap.put("success",false);
                        modelMap.put("Msg","完成采购审批操作成功！");
                        modelMap.put("purchase",purchase);
                        title="采购申请失败";
                        text="审批了你提交的采购申请为未通过，请看审批意见！";
                        User requestor=userRepository.findByLoginName(purchase.getRequestor().getLoginName());
                        if(requestor!=null&&requestor.getEmail()!=null){
                            text = approver.getLoginName() + text ;
                            mailService.sendEmail(requestor.getEmail(),title,text);
                        }
                    } break;
                    default:{
                        log.error(approver.getLoginName()+"采购审批传入的状态不对");
                        modelMap.put("success",false);
                        modelMap.put("Msg","传入的审批状态为空或者不对，审批采购申请失败！");
                    }  break;
                }
                purchase.setApprover(approver);
                purchase.setExeState(purchaseModel.getExeState());
                purchase.setApprovalAdvice(purchaseModel.getApprovalAdvice());
                purchase.setApprovalTime(LocalDate.now());
                purchaseRepository.save(purchase);
            }else {
                log.error("采购审批传入的id不对");
                modelMap.put("success",false);
                modelMap.put("Msg","传入的采购申请id为空，审批采购清单失败！");
            }
        }else {
            log.error("采购审批传入的申请单为空");
            modelMap.put("success",false);
            modelMap.put("Msg","传入采购申请为空，审批采购清单失败！");
        }
        return modelMap;
    }
    /**
     * 采购完成采购确认（采购调用）
     * 1.当进行采购下单后需调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> finishPurchase(PurchaseModel purchaseModel,Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        if(!StringUtils.isEmpty(purchaseModel)){
            if(purchaseRepository.existsById(purchaseModel.getPurchaseId())){
                Purchase purchase=purchaseRepository.getOne(purchaseModel.getPurchaseId());
                purchase.setExeState(4);
                purchase.setOrderDate(LocalDate.now());
                List<DetailPurchaseModel> detailPurchaseModels=purchaseModel.getDetailPurchaseModels();
                List<DetailPurchase>detailPurchases=purchase.getDetailPurchases();
                for (DetailPurchaseModel detailPurchaseModel:detailPurchaseModels) {
                    for (DetailPurchase detailPurchase:detailPurchases) {
                        if(detailPurchaseModel.getDetailPurchaseId()==detailPurchase.getDetailPurchaseId()){
                            detailPurchase.setPurchasePrice(detailPurchaseModel.getPurchasePrice());
                            detailPurchase.setPurchaseCount(detailPurchaseModel.getPurchaseCount());
                            detailPurchase.setOrderId(detailPurchaseModel.getOrderId());
                            detailPurchase.setContractNumber(detailPurchaseModel.getContractNumber());
                            detailPurchase.setRemark(detailPurchaseModel.getRemark());
                            detailPurchase.setExpirationDate(detailPurchaseModel.getExpirationDate());
                            DetailOutStorage detailOutStorage=detailPurchase.getDetailOutStorage();
                            if(detailOutStorage!=null){
                                OutStorage outStorage=detailOutStorage.getOutStorage();
                                if(outStorage!=null){
                                    outStorage.setBrderNumber(detailPurchaseModel.getOrderId());
                                    outStorageRepository.save(outStorage);
                                }

                            }
                        }
                    }
                }
                List<Role> roles=new ArrayList<>();
                List<User> inspectors =new ArrayList<>();
                Role role=roleRepository.findByRole("ROLE_INSPECTOR");
                roles.add(role);
                if(!roles.isEmpty()) {
                    inspectors = userRepository.findByRoles( roles);
                }
                String title="物料采购完成完成！";
                String text="物料采购完成，可以进行质检操作了！";
                if(!inspectors.isEmpty()) {
                    for (User inspector : inspectors) {
                        if (inspector.getEmail() != null) {
                            mailService.sendEmail(inspector.getEmail(), title, text);
                        }
                    }
                }

                modelMap.put("success",true);
                modelMap.put("Msg","完成采购操作成功！");
                modelMap.put("purchase",purchase);
                purchaseRepository.save(purchase);
            }else {
                modelMap.put("success",false);
                modelMap.put("Msg","传入的采购申请id为空，完成采购操作失败！");
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","传入采购申请为空，完成采购操作失败！");
        }
        return modelMap;
    }
    /**等待质检（采购调用）
     * 1.当进行采购完成需要通知质检检验时调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> waitQuality(PurchaseModel purchaseModel, Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        String title="完成了采购";
        String text="完成了采购，希望质检可以质检一下，谢谢！";
        if(!StringUtils.isEmpty(purchaseModel)){
            if(purchaseRepository.existsById(purchaseModel.getPurchaseId())){
                Purchase purchase=purchaseRepository.getOne(purchaseModel.getPurchaseId());
                purchase.setExeState(5);
                purchase.setReceivingDate(LocalDate.now());
                modelMap.put("success",false);
                modelMap.put("Msg","完成采购操作成功！");
                modelMap.put("purchase",purchase);
                User user=userRepository.findByLoginName(principal.getName());
                log.info(user.getLoginName()+"完成通知质检操作");
                List<Role> roles=new ArrayList<>();
                List<User> examiners =new ArrayList<>();
                Role role=roleRepository.findByRole("ROLE_STOREHOUSE");
                roles.add(role);
                if(!roles.isEmpty()) {
                    examiners = userRepository.findByRoles( roles);
                }
                if(!examiners.isEmpty()) {
                    for (User examiner:examiners) {
                        if(examiner.getEmail()!=null){
                            text = user.getLoginName() + text ;
                            mailService.sendEmail(examiner.getEmail(),title,text);
                        }
                    }
                }else {
                    title="质检员身份不存在";
                    text="质检员身份不存在，请通知系统管理员完善用户管理，并电话通知质检一下，谢谢！";
                    if(user.getEmail()!=null){
                        mailService.sendEmail(user.getEmail(),title,text);
                    }

                }
                purchaseRepository.save(purchase);
            }else {
                log.error("完成采购操作传入的采购单id为空");
                modelMap.put("success",false);
                modelMap.put("Msg","传入的采购申请id为空，完成采购操作失败！");
            }
        }else {
            log.error("完成采购操作传入的采购单为空");
            modelMap.put("success",false);
            modelMap.put("Msg","传入采购申请为空，完成采购操作失败！");
        }
        return modelMap;
    }
    /**
     * 查找所有采购
     * 1.传入参数为空
     * 2.查找结果为所有采购的列表
     * @return
     */
    @Override
    public Map<String, Object> findAllPurchase() {
        Map<String, Object> modelMap=new HashMap<>();
        List<Purchase> purchases=purchaseRepository.findAll();
        List<PurchaseModel>purchaseModels=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase purchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                purchaseModels.add(purchaseModel);
            }
            modelMap.put("success",false);
            modelMap.put("Msg","获取采购清单成功！");
            modelMap.put("PurchaseModels",purchaseModels);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","采购记录为空，获取采购清单失败");
        }
        return modelMap;
    }
    /**通过条件查询采购
     * 1.传入参数：条件列表参考FindPurchaseModel
     * 2.完成通过给定的条件查询采购
     * @param findPurchaseModels
     * @return
     */
    @Override
    public Map<String, Object> findCondition(List<FindPurchaseModel> findPurchaseModels) {
        Map<String, Object> modelMap=new HashMap<>();
        List<Purchase> purchases=new ArrayList<>();
        List<Integer> purchaseIdList=new ArrayList<>();
        if(!findPurchaseModels.isEmpty()){
            String sql="select distinct purchase_id from find_purchase";
            String value=" where ";
            for (FindPurchaseModel findPurchaseModel:findPurchaseModels) {
                if(findPurchaseModel.getName()!=null&&findPurchaseModel.getOperation()!=null&&findPurchaseModel.getOperation().equals("between"))
                {
                        if(findPurchaseModel.getSmall()!=null&&findPurchaseModel.getLarge()!=null){
                            value=value+findPurchaseModel.getName()+" between '"+findPurchaseModel.getSmall()+"' and '"+findPurchaseModel.getLarge()+"' and ";
                        }
                        else if(findPurchaseModel.getSmall()==null&&findPurchaseModel.getLarge()!=null){
                            value=value+findPurchaseModel.getName()+"<='"+findPurchaseModel.getLarge()+"' and ";
                        }else if(findPurchaseModel.getSmall()!=null&&findPurchaseModel.getLarge()==null){
                            value=value+findPurchaseModel.getName()+">='"+findPurchaseModel.getSmall()+"' and ";
                        }else {
                            value=value;
                        }
                }else if(findPurchaseModel.getName()!=null&&findPurchaseModel.getOperation()!=null&&findPurchaseModel.getOperation().equals("equal")){
                    value=value+findPurchaseModel.getName();
                    switch (findPurchaseModel.getName()) {
                        case "requestor_id": {
                            if (findPurchaseModel.getSmall() != null) {
                                User requestor = userRepository.findByLoginName(findPurchaseModel.getSmall());
                                if (requestor != null)
                                    value = value+"=" + requestor.getId().toString()+" and ";
                            }
                        }
                        break;
                        case "auditor_id": {
                            if (findPurchaseModel.getSmall() != null) {
                                User requestor = userRepository.findByLoginName(findPurchaseModel.getSmall());
                                if (requestor != null)
                                    value = value+"=" + requestor.getId().toString()+" and ";
                            }
                        }
                        break;
                        case "approver_id": {
                            if (findPurchaseModel.getSmall() != null) {
                                User requestor = userRepository.findByLoginName(findPurchaseModel.getSmall());
                                if (requestor != null)
                                    value =value+"="+ requestor.getId().toString()+" and ";
                            }
                        }break;
                        default:value =value+" ='" + findPurchaseModel.getSmall().toString()+"' and ";
                    }
                }
            }
            if(!value.equals(" where ")){
                sql = sql +" "+ value ;
                sql=sql.substring(0,sql.length() - 5);
            }
           purchaseIdList=em.createNativeQuery(sql).getResultList();
            for (Integer purchaseId:purchaseIdList) {
                Purchase purchase=purchaseRepository.getOne(purchaseId);
                purchases.add(purchase);
            }
        }else {
            purchases=purchaseRepository.findAll();
        }
        List<PurchaseModel>purchaseModels=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase purchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                purchaseModels.add(purchaseModel);
            }
            modelMap.put("success",false);
            modelMap.put("Msg","获取采购清单成功！");
            modelMap.put("PurchaseModels",purchaseModels);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","采购记录为空，获取采购清单失败");
        }
        return modelMap;
    }

    /**通过Id查找采购
     * @param purchaseModel
     * @return
     */
    @Override
    public Map<String, Object> findPurchaseById(PurchaseModel purchaseModel) {
        Map<String, Object> modelMap=new HashMap<>();

        return modelMap;
    }
    /**
     * 提交查找（由采购人员调用当前接口）
     * 1.实现查找我保存的但是未提交的采购申请
     * 2.实现查找我历史提交的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyApplyPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        User user=userRepository.findByLoginName(principal.getName());
        List<Purchase>purchases=purchaseRepository.findByRequestorAndExeState(user,0);
        List<PurchaseModel>MyApplyPurchase=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase purchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyApplyPurchase.add(purchaseModel);
                modelMap.put("MyApplyPurchase",MyApplyPurchase);
            }
        }else{
            modelMap.put("Msg","我待提交的采购申请为空！");
        }
        List<Integer>exestates=new ArrayList<>();
        exestates.add(1);
        exestates.add(2);
        exestates.add(3);
        exestates.add(4);
        exestates.add(5);
        exestates.add(6);
        exestates.add(7);
        List<Purchase>historyPurchases=purchaseRepository.findByRequestorAndExeStateIn(user,exestates);
        List<PurchaseModel>MyHistoryApplyPurchase=new ArrayList<>();
        if(!historyPurchases.isEmpty()){
            for (Purchase purchase:historyPurchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyHistoryApplyPurchase.add(purchaseModel);
                modelMap.put("MyHistoryApplyPurchase",MyHistoryApplyPurchase);
            }
        }else{
            modelMap.put("Msg","我的历史采购申请为空！");
        }
        return modelMap;
    }
    /**
     * 审核查找（由财务经理调用当前接口）
     * 1.实现查找我能审核的采购申请
     * 2.实现查找我历史审核的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyApprovalPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        User user=userRepository.findByLoginName(principal.getName());
        List<Purchase>purchases=purchaseRepository.findByExeState(1);
        List<PurchaseModel>MyApprovalPurchases=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase purchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyApprovalPurchases.add(purchaseModel);
                modelMap.put("MyApprovalPurchases",MyApprovalPurchases);
            }
        }else {
            modelMap.put("Msg","待审核的采购申请为空！");
        }
        List<Purchase>historyPurchases=purchaseRepository.findByAuditor(user);
        List<PurchaseModel>MyHistoryApprovalPurchases=new ArrayList<>();
        if(!historyPurchases.isEmpty()){
            for (Purchase purchase:historyPurchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyHistoryApprovalPurchases.add(purchaseModel);
                modelMap.put("MyHistoryApprovalPurchases",MyHistoryApprovalPurchases);
            }
        }else {
            modelMap.put("Msg","我审核的采购记录为空！");
        }
        return modelMap;
    }
    /**
     * 审批查询（由高管调用当前接口）
     * 1.实现查找我能审批的采购申请
     * 2.实现查找我历史审批的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findHighApprovalPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        User user=userRepository.findByLoginName(principal.getName());
        List<Purchase>purchases=purchaseRepository.findByApproverAndExeState(user,2);
        List<PurchaseModel>MyHighApprovalPurchases=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase purchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyHighApprovalPurchases.add(purchaseModel);
                modelMap.put("MyHighApprovalPurchases",MyHighApprovalPurchases);
            }
        }else {
            modelMap.put("Msg","待审批的采购申请为空！");
        }
        List<Integer>exeStates=new ArrayList<>();
        exeStates.add(3);
        exeStates.add(4);
        exeStates.add(5);
        exeStates.add(6);
        exeStates.add(7);
        exeStates.add(8);
        List<Purchase>historyPurchases=purchaseRepository.findByApproverAndExeStateIn(user,exeStates);
        List<PurchaseModel>MyHistoryHighApprovalPurchases=new ArrayList<>();
        if(!historyPurchases.isEmpty()){
            for (Purchase purchase:historyPurchases) {
                PurchaseModel purchaseModel=purchaseToModel(purchase);
                MyHistoryHighApprovalPurchases.add(purchaseModel);
                modelMap.put("MyHistoryHighApprovalPurchases",MyHistoryHighApprovalPurchases);
            }
        }else {
            modelMap.put("Msg","我审批的采购记录为空！");
        }
        return modelMap;
    }
    /**
     * 待采购查询（由采购调用当前接口）
     * 1.实现查找我已审批的采购处于等待采购的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findWaitPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<Purchase>purchases=purchaseRepository.findByExeState(3);
        List<PurchaseModel>purchaseModels=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase putchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(putchase);
                purchaseModels.add(purchaseModel);
            }
            modelMap.put("findWaitPurchase",purchaseModels);
        }else {
            modelMap.put("Msg","已审批完成待采购的采购申请为空！");
        }
        return modelMap;
    }
    /**
     * 采购完成查询（由采购调用当前接口）（已采购但未到货）
     * 1.实现查找我已完成的采购处于等待通知质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findFinishPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<Purchase>purchases=purchaseRepository.findByExeState(4);
        List<PurchaseModel>purchaseModels=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase putchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(putchase);
                purchaseModels.add(purchaseModel);
            }
            modelMap.put("finishPurchase",purchaseModels);
        }else {
            modelMap.put("Msg","已完成采购但未通知质检的采购申请为空！");
        }
        return modelMap;
    }
    /**待质检查询（由质检调用当前接口）
     * 1.实现查找我已采购完成的采购处于等待质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findWaitQuality(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<Purchase>purchases=purchaseRepository.findByExeState(5);
        List<PurchaseModel>purchaseModels=new ArrayList<>();
        if(!purchases.isEmpty()){
            for (Purchase putchase:purchases) {
                PurchaseModel purchaseModel=purchaseToModel(putchase);
                purchaseModels.add(purchaseModel);
            }
            modelMap.put("waitQuality",purchaseModels);
        }else {
            modelMap.put("Msg","已采购并已经通知质检去质检的采购申请为空！");
        }
        return modelMap;
    }
    /**
     * 显示和实体转换
     * 实现将界面的采购转换为实体的采购
     * @param purchaseModel
     * @return
     */
    @Override
    public Purchase modelToPurchase(PurchaseModel purchaseModel) {
        Purchase purchase = new Purchase();
          purchase.setPurchaseId(purchaseModel.getPurchaseId());
          purchase.setName(purchaseModel.getName());
          purchase.setEndDate(purchaseModel.getEndDate());
          List<DetailPurchaseModel> detailPurchaseModels = purchaseModel.getDetailPurchaseModels();
          List<DetailPurchase> detailPurchases = new ArrayList<>();
          if (!detailPurchaseModels.isEmpty()) {
              List<OutStorage>outStorages=new ArrayList<>();
              for (DetailPurchaseModel detailPurchaseModel:detailPurchaseModels) {
                  DetailPurchase detailPurchase=new DetailPurchase();
                  if(detailPurchaseModel.getDetailPurchaseId()!=null) {
                      detailPurchase.setDetailPurchaseId(detailPurchaseModel.getDetailPurchaseId());
                  }
                  DetailOutStorage detailOutStorage=detailOutStorageRepository.getOne(detailPurchaseModel.getDetailOutStorageId());
                  OutStorage outStorage=detailOutStorage.getOutStorage();
                  outStorages.add(outStorage);
                  detailPurchase.setDetailOutStorage(detailOutStorage);
                  detailPurchase.setReplaceModel(detailPurchaseModel.getReplaceModel());
                  detailPurchase.setProgramNumber(detailPurchaseModel.getProgramNumber());
                  detailPurchase.setPurchaseModel(detailPurchaseModel.getPurchaseModel());
                  detailPurchase.setReqCount(detailPurchaseModel.getReqCount());
                  detailPurchase.setReqPrice(detailPurchaseModel.getReqPrice());
                  detailPurchase.setPurchaseCount(detailPurchaseModel.getPurchaseCount());
                  detailPurchase.setRemark(detailPurchaseModel.getRemark());
                  detailPurchase.setContractNumber(detailPurchaseModel.getContractNumber());
                  detailPurchase.setExpirationDate(detailPurchaseModel.getExpirationDate());
                  if(detailPurchaseModel.getPurchasePrice()==null){
                    detailPurchase.setPurchasePrice(detailPurchaseModel.getReqPrice());
                  }else
                  detailPurchase.setPurchasePrice(detailPurchaseModel.getPurchasePrice());
                  detailPurchase.setOrderId(detailPurchaseModel.getOrderId());
                  Seller seller=sellerRepository.findBySellerName(detailPurchaseModel.getSellerName());
                  detailPurchase.setSeller(seller);
                  Material material=new Material();
                  material.setMaterialId(detailPurchaseModel.getMaterialId());
                  material.setName(detailPurchaseModel.getName());
                  material.setCode(detailPurchaseModel.getCode());
                  material.setModelNumber(detailPurchaseModel.getModelNumber());
                  detailPurchase.setMaterial(material);
                  detailPurchases.add(detailPurchase);
              }
              purchase.setOutStorages(outStorages);
              purchase.setDetailPurchases(detailPurchases);
          }
       return purchase;
    }
    /**
     *  显示和实体转换
     * 实现将实体的采购转换为界面的采购
     * @param purchase
     * @return
     */
    @Override
    public PurchaseModel purchaseToModel(Purchase purchase) {
        PurchaseModel purchaseModel=new PurchaseModel();
        purchaseModel.setPurchaseId(purchase.getPurchaseId());
        purchaseModel.setName(purchase.getName());
        purchaseModel.setCreatDate(purchase.getSubmitDate());
        purchaseModel.setEndDate(purchase.getEndDate());
        purchaseModel.setExeState(purchase.getExeState());
        purchaseModel.setAuditAdvice(purchase.getAuditAdvice());
        purchaseModel.setApprovalAdvice(purchase.getApprovalAdvice());
        purchaseModel.setOrderDate(purchase.getOrderDate());
        purchaseModel.setReceivingDate(purchase.getReceivingDate());
        purchaseModel.setQADate(purchase.getQADate());
        purchaseModel.setInStorageDate(purchase.getInStorageDate());
        List<DetailPurchase>detailPurchases=purchase.getDetailPurchases();
        List<DetailPurchaseModel> detailPurchaseModels=new ArrayList<>();
        int typeNum=0;
        int totalAmount=0;
        float totalPrice=0;
        if(!detailPurchases.isEmpty()){
            for (DetailPurchase detailPurchase:detailPurchases) {
                DetailPurchaseModel detailPurchaseModel=new DetailPurchaseModel();
                detailPurchaseModel.setDetailPurchaseId(detailPurchase.getDetailPurchaseId());
                if(detailPurchase.getMaterial()!=null){
                    detailPurchaseModel.setName(detailPurchase.getMaterial().getName());
                    detailPurchaseModel.setCode(detailPurchase.getMaterial().getCode());
                    detailPurchaseModel.setModelNumber(detailPurchase.getMaterial().getModelNumber());
                    detailPurchaseModel.setMaterialId(detailPurchase.getMaterial().getMaterialId());
                    if(detailPurchase.getMaterial().getType()!=null)
                        detailPurchaseModel.setMaterialType(detailPurchase.getMaterial().getType().getTypeName());
                }
                detailPurchaseModel.setEncapsulation(detailPurchase.getMaterial().getEncapsulation());
                detailPurchaseModel.setReplaceModel(detailPurchase.getReplaceModel());
                detailPurchaseModel.setPurchaseModel(detailPurchase.getPurchaseModel());
                detailPurchaseModel.setOrderId(detailPurchase.getOrderId());
                detailPurchaseModel.setRemark(detailPurchase.getRemark());
                detailPurchaseModel.setExpirationDate(detailPurchase.getExpirationDate());
                detailPurchaseModel.setContractNumber(detailPurchase.getContractNumber());
                if(detailPurchase.getSeller()!=null)
                detailPurchaseModel.setSellerName(detailPurchase.getSeller().getSellerName());
                detailPurchaseModel.setReqCount(detailPurchase.getReqCount());
                detailPurchaseModel.setReqPrice(detailPurchase.getReqPrice());
                detailPurchaseModel.setPurchaseCount(detailPurchase.getPurchaseCount());
                detailPurchaseModel.setPurchasePrice(detailPurchase.getPurchasePrice());
                detailPurchaseModel.setProgramNumber(detailPurchase.getProgramNumber());
                if(detailPurchase.getDetailOutStorage()!=null)
                detailPurchaseModel.setDetailOutStorageId(detailPurchase.getDetailOutStorage().getDetailOutStorageId());
                List<DetailQualityAudit>detailQualityAudits=detailPurchase.getDetailQualityAudits();
                Float passRate=0.0f;
                int count=0;
                int instorageNumber=0;
                if(!detailQualityAudits.isEmpty()){
                    for (DetailQualityAudit detailQualityAudit:detailQualityAudits) {
                         count=++count;
                         if(detailQualityAudit.getPassRate()!=null)
                         passRate =passRate +detailQualityAudit.getPassRate();
                         if(detailQualityAudit.getInStorageCount()!=null)
                         instorageNumber=instorageNumber+detailQualityAudit.getInStorageCount();
                         passRate=passRate/count;
                    }
                }
                detailPurchaseModel.setInStorageCount(instorageNumber);
                detailPurchaseModel.setPassRate(passRate);
                if(detailPurchase.getReqPrice()!=null&&detailPurchase.getReqCount()!=null) {
                    detailPurchaseModel.setTotalPrice(detailPurchase.getReqCount() * detailPurchase.getReqPrice());
                }else {
                    detailPurchaseModel.setTotalPrice(0f);
                }
                typeNum=typeNum+1;
                if(detailPurchase.getReqCount()!=null)
                totalAmount=totalAmount+detailPurchase.getReqCount();
                if(detailPurchase.getReqPrice()!=null&&detailPurchase.getReqCount()!=null)
                totalPrice=totalPrice+detailPurchase.getReqCount()*detailPurchase.getReqPrice();
                detailPurchaseModels.add(detailPurchaseModel);
            }
        }
        if(purchase.getRequestor()!=null)
        purchaseModel.setRequestor(purchase.getRequestor().getLoginName());
        if(purchase.getAuditor()!=null)
            purchaseModel.setAuditor(purchase.getAuditor().getLoginName());
        if(purchase.getApprover()!=null)
            purchaseModel.setApprover(purchase.getApprover().getLoginName());

        purchaseModel.setTypeNum(typeNum);
        purchaseModel.setTotalAmount(totalAmount);
        purchaseModel.setTotalPrice(totalPrice);
        purchaseModel.setDetailPurchaseModels(detailPurchaseModels);
        return purchaseModel;
    }

    /**数据库生成view
     * @throws Exception
     */
    @Transactional
    @PostConstruct
    public void init() throws Exception {
        System.out.println("JavaBean类init 方法");
        String creatFindDetailPurchaseView="create VIEW find_detail_purchase as select `detail_purchase`.`detail_purchase_id` AS `detail_purchase_id`" +
                ",sum(`detail_purchase`.`purchase_count`) AS `total_count`," +
                "sum((`detail_purchase`.`purchase_count` * `detail_purchase`.`purchase_price`)) AS `total_price`," +
                "`detail_purchase`.`purchase` AS `purchase` " +
                "from `detail_purchase` " +
                "group by `detail_purchase`.`purchase`";
        CreateView createViewclass=new CreateView();
        createViewclass.createView(creatFindDetailPurchaseView);
        String creatFindPurchaseView="create VIEW find_purchase as select distinct `purchase`.`purchase_id` AS `purchase_id`," +
                "`purchase`.`approval_time` AS `approval_time`," +
                "`purchase`.`audit_date` AS `audit_date`," +
                "`purchase`.`creat_date` AS `creat_date`," +
                "`purchase`.`end_date` AS `end_date`," +
                "`purchase`.`exe_state` AS `exe_state`," +
                "`purchase`.`in_storage_date` AS `in_storage_date`," +
                "`purchase`.`order_date` AS `order_date`," +
                "`purchase`.`qadate` AS `qadate`," +
                "`purchase`.`receiving_date` AS `receiving_date`," +
                "`purchase`.`submit_date` AS `submit_date`," +
                "`purchase`.`approver_id` AS `approver_id`," +
                "`purchase`.`auditor_id` AS `auditor_id`," +
                "`purchase`.`requestor_id` AS `requestor_id`," +
                "`find_detail_purchase`.`total_price` AS `total_price`," +
                "`find_detail_purchase`.`total_count` AS `total_count`," +
                "`detail_quality_audit`.`pass_rate` AS `pass_rate` " +
                "from ((`purchase` left join `find_detail_purchase` on((`purchase`.`purchase_id` = `find_detail_purchase`.`purchase`))) left join `detail_quality_audit` on((`detail_quality_audit`.`detail_purchase` = `find_detail_purchase`.`detail_purchase_id`)))";
        createViewclass.createView(creatFindPurchaseView);
        String creatFindStorageView="create VIEW find_storage as select `storage`.`storage_id` AS `storage_id`," +
                "`storage`.`price` AS `price`," +
                "`storage`.`amount` AS `amount`," +
                "`storage`.`sequence` AS `sequence`," +
                "`storage`.`storeage_name` AS `storeage_name`," +
                "`storage`.`produce_factory_name` AS `produce_factory_name`," +
                "`storage`.`end_date` AS `end_date`," +
                "`storage`.`material` AS `material`," +
                "`material`.`encapsulation` AS `encapsulation`," +
                "`material`.`model_number` AS `model_number`," +
                "`material`.`code` AS `code`," +
                "`material`.`type` AS `type`," +
                "`storage`.`program_number` AS `program_number`," +
                "`storage`.`seller` AS `seller` " +
                "from (`storage` join `material`) " +
                "where (`storage`.`material` = `material`.`material_id`)";
        createViewclass.createView(creatFindStorageView);
    }
}
