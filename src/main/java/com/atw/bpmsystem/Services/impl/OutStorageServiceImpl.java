package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Common.RequestUtil;
import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.DetailStorageModel;
import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.MailService;
import com.atw.bpmsystem.Services.OutStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import javax.transaction.Transactional;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
@Slf4j
@Service
@EnableTransactionManagement
public class OutStorageServiceImpl implements OutStorageService {
    @Autowired
    private OutStorageRepository outStorageRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private DetailOutStorageRepository detailOutStorageRepository;

    @Autowired
    private OutStoreKeeperRepository outStoreKeeperRepository;

    /**新建出库（开发人员调用）
     * 1.需要传入的参数出库界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.实现出库单的保存操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addOutStorage(OutStorageModel outStorageModel,Principal principal) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(outStorageModel)){
            outStorageModel.setExeState(0);
            OutStorage outStorage=modelToOutStorage(outStorageModel);
            User user=userRepository.findByLoginName(principal.getName());
            if(!StringUtils.isEmpty(outStorage)) {
                //如果原来新建了出库单，则修改
                if (outStorageModel.getOutStorageId() != null && outStorageRepository.existsById(outStorageModel.getOutStorageId())) {
                    modelMap.put("Msg", "出库单保存成功");
                    modelMap.put("success", true);
                    log.debug(user.getLoginName()+"修改出库单！"+JSON.toJSONString(outStorageModel));
                }//如果原来没有出库单，则新建出库单
                else {
                    outStorage.setRequestor(user.getLoginName());
                    String temp_str = "";
                    Date dt = new Date();
                    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                    temp_str = sdf.format(dt);
                    String requestNo = "OUT" + temp_str;
                    outStorage.setRequestNo(requestNo);
                    outStorage.setCreateDate(LocalDate.now());
                    outStorage.setDepartment(user.getOperation());
                    modelMap.put("Msg", "出库单生成成功");
                    modelMap.put("success", true);
                    log.info(user.getLoginName()+"新建出库单！"+JSON.toJSONString(outStorageModel));

                }
                outStorageRepository.save(outStorage);

                List<DetailOutStorage> detailOutStorages=outStorage.getDetailOutStorages();
                for (DetailOutStorage detailOutStorage:detailOutStorages) {
                    detailOutStorage.setOutStorage(outStorage);
                }
                modelMap.put("OutStorage",outStorage);
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "出库单生成失败，请输入出库物料");
                log.error("传入出库单为空，保存或者新建出库单失败！");
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "出库单生成失败，请输入出库物料");
            log.error("传入出库单为空，保存或者新建出库单失败！");
        }

        return modelMap;
    }
    /**
     * 出库单的提交（开发人员调用）
     * 1.需要传入的参数outStorageId
     * 2.实现单的提交操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> saveOutStorage(OutStorageModel outStorageModel,Principal principal) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String title="出库单申请！";
        String text="提交了出库单申请，希望经理可以审核一下，谢谢！";
        User user=userRepository.findByLoginName(principal.getName());
        log.info(user.getLoginName()+"提交了出库单！"+JSON.toJSONString(outStorageModel));
        if(!StringUtils.isEmpty(outStorageModel)){
        OutStorage outStorage = modelToOutStorage(outStorageModel);
            //如果原来新建了的出库单，则修改后提交
            if(outStorage.getOutStorageId()!=null&&outStorageRepository.existsById(outStorage.getOutStorageId())) {

                modelMap.put("success", true);
                modelMap.put("Msg", "出库单提交成功！");
            }//如果原来没有新建的出库单，直接新建后提交
            else {
                String temp_str="";
                Date dt = new Date();
                //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                temp_str=sdf.format(dt);
                String requestNo="OUT_"+temp_str;
                outStorage.setRequestNo(requestNo);
                outStorage.setRequestor(user.getLoginName());
                outStorage.setCreateDate(LocalDate.now());
                outStorage.setDepartment(user.getOperation());
                modelMap.put("success", true);
                modelMap.put("Msg", "该出库单原来不存在，新生成出库单提交成功！");
            }
            outStorage.setExaminer("");
            outStorage.setExeStat(1);
            outStorage.setApprover("");
            outStorage.setAuditDate(null);
            outStorage.setApproveDate(null);
            outStorage.setAuditor("");

            outStorageRepository.save(outStorage);

            List<DetailOutStorage> detailOutStorages=outStorage.getDetailOutStorages();
            for (DetailOutStorage detailOutStorage:detailOutStorages) {
                detailOutStorage.setOutStorage(outStorage);
            }
            List<Role> roles=new ArrayList<>();
            List<User> examiners =new ArrayList<>();
            Role role=roleRepository.findByRole("ROLE_DIVISIONMANAGER");
            roles.add(role);
            if(!roles.isEmpty()) {
                examiners = userRepository.findByOperationAndRoles(user.getOperation(), roles);
            }
            if(!examiners.isEmpty()) {
                text=user.getLoginName()+text ;
                for (User examiner:examiners) {
                    if(examiner.getEmail()!=null){
                        mailService.sendEmail(examiner.getEmail(),title,text);
                    }
                }
            }else {
                if(user.getEmail()!=null)
                    mailService.sendEmail(user.getEmail(),title,text);
            }

            modelMap.put("OutStorage",outStorage);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "该出库单不存在，出库单提交失败！");
        }
        return modelMap;
    }
    /**
     * 删除出库单
     * 1.需要传入的参数id的表单
     * 2.实现出库单的删除操作
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteOutStorage(List<Map<String,Integer>> idList) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        for (Map<String,Integer> ids:idList) {
            if(outStorageRepository.existsById(ids.get("id"))){
                outStorageRepository.existsById(ids.get("id"));
                modelMap.put("success", true);
                modelMap.put("Msg", "出库单删除成功");
                modelMap.put("OutStorage",outStorageRepository.getOne(ids.get("id")));
                outStorageRepository.deleteById(ids.get("id"));
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "该出库单不存在，出库单删除失败");
            }
        }
        return modelMap;
    }
    /**
     * 出库单修改
     * 1.需要传入的参数utStorageId以及界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.完成出库单修改操作
     * @param outStorageModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyOutStorage(OutStorageModel outStorageModel) {
        OutStorage outStorage=modelToOutStorage(outStorageModel);
        outStorage.setOutStorageId(outStorageModel.getOutStorageId());
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(outStorageRepository.existsById(outStorage.getOutStorageId())){
            outStorageRepository.save(outStorage);
            modelMap.put("success", true);
            modelMap.put("Msg", "出库单修改成功");
            modelMap.put("OutStorage",outStorage);
        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "该出库单不存在，出库单修改失败");
        }
        return modelMap;

    }
    /**
     * 审核出库（部门经理操作）
     * 1.传入参数outStorageId，approver,auditComments,exeState,histories
     * 2.完成出库单审核操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> divisionManagerApproval(OutStorageModel outStorageModel,Principal principal) {
       Map<String,Object>modelMap=new HashMap<>();
       String title="出库单审批！";
       String text="审核了出库单，需要上级审批，希望领导审批一下，谢谢！";
       User user=userRepository.findByLoginName(principal.getName());
       if(outStorageModel.getOutStorageId()!=null){
       OutStorage outStorage=outStorageRepository.getOne(outStorageModel.getOutStorageId());
      // List<OutStorage> outStorages=outStorageRepository.findByDepartmentAndExeStat(user.getOperation(),1);
         if(!StringUtils.isEmpty(outStorage)){
               outStorage.setExaminer(principal.getName());
               outStorage.setHistories(outStorage.getHistories()+outStorageModel.getHistories());
               outStorage.setExeStat(outStorageModel.getExeState());
               outStorage.setAuditComments(outStorageModel.getAuditComments());
               outStorage.setAuditDate(LocalDate.now());
               switch (outStorageModel.getExeState()){
               case 2:{//请报上级
                   log.debug("出库审核为请报上级（待上级审批）");
                   if (outStorageModel.getApprover() != null) {
                       User approval = userRepository.findByLoginName(outStorageModel.getApprover());
                       if(approval!=null)
                       outStorage.setApprover(approval.getLoginName());
                       modelMap.put("success", true);
                       modelMap.put("Msg", "出库单审核成功，转入审批状态！");
                       modelMap.put("OutStorage", outStorage);
                       log.info(user.getLoginName()+"审核出库申请到上级处！"+outStorageModel.getRemark());
                       if(approval!=null&&approval.getEmail()!=null){
                           text=user.getLoginName()+text ;
                           mailService.sendEmail(approval.getEmail(),title,text);
                       }
                   }
               }break;
               case 3:{//同意
                   log.info(user.getLoginName()+"出库审核为同意（待出库）"+outStorageModel.getRemark(),outStorageModel.toString());
                   outStorage.setApprover(user.getLoginName());
                   outStorage.setApproveDate(LocalDate.now());
                   outStorage.setApproveComments(outStorageModel.getAuditComments());
                   modelMap.put("success", true);
                   modelMap.put("Msg", "出库单审核成功，转入待出库状态！");
                   modelMap.put("OutStorage", outStorage);
                   title="出库单审批！";
                   text="审批通过了出库单，请库房出库一下！";

                   List<Role> roles=new ArrayList<>();
                   Role role=roleRepository.findByRole("ROLE_STOREHOUSE");
                   roles.add(role);
                   List<User> users=userRepository.findByRoles(roles);
                   for (User auditor:users) {
                       text=user.getLoginName()+text ;
                       mailService.sendEmail(auditor.getEmail(),title,text);
                   }
                  // outStorage.setApproveComments(outStorageModel.getApproveComments());
               }break;
               case 4:{//待采购

                   modelMap.put("success", true);
                   modelMap.put("Msg", "出库单审核成功，转入等待状态！");
                   modelMap.put("OutStorage", outStorage);
                   outStorage.setApprover(user.getLoginName());
                   outStorage.setApproveComments(outStorageModel.getAuditComments());
                   outStorage.setApproveDate(LocalDate.now());
                   log.info(user.getLoginName()+"审核出库申请单为等待"+outStorageModel.getRemark());
                   title="出库单审批！";
                   text="审批出库单为等待，库房物料不足，需要采购购买一下！";
                   List<Role> roles=new ArrayList<>();
                   Role role=roleRepository.findByRole("ROLE_BUYER");
                   roles.add(role);
                   List<User> users=userRepository.findByRoles(roles);
                   if(!users.isEmpty()){
                       for (User auditor:users) {
                           if(auditor.getEmail()!=null) {
                               text = user.getLoginName() + text ;
                               mailService.sendEmail(auditor.getEmail(), title, text);
                           }
                       }
                   }
//                   User auditor=userRepository.findByLoginName(outStorageModel.getAuditor());
//                   mailService.sendEmail(auditor.getEmail(),title,text);
               } break;
               case 7:{//关闭
                   log.debug("出库审核为待关闭");
                   modelMap.put("success", true);
                   modelMap.put("Msg", "出库单审核成功，跳入关闭状态！");
                   outStorage.setCloseReason("审核拒绝！");
                   modelMap.put("OutStorage", outStorage);
                   User requestor=userRepository.findByLoginName(outStorage.getRequestor());
                   title="出库单审批！";
                   text="审批出库单关闭，详情原因请看申请单的备注原因！";
                   log.info(user.getLoginName()+"审核出库申请单为关闭 "+outStorageModel.getRemark());
                   if(requestor!=null&&requestor.getEmail()!=null){
                       text = user.getLoginName() + text ;
                       mailService.sendEmail(requestor.getEmail(),title,text);
                   }

               }break;
               case 0:{//待修改
                   log.info(user.getLoginName()+"出库审核为待修改 "+outStorageModel.getRemark());
                   modelMap.put("success", true);
                   modelMap.put("Msg", "出库单审核成功");
                   modelMap.put("OutStorage", outStorage);
                   User requestor=userRepository.findByLoginName(outStorage.getRequestor());
                   text="审批没有通过，详情原因请看申请单的备注原因！";
                   if(requestor!=null&&requestor.getEmail()!=null){
                       text = user.getLoginName() + text ;
                       mailService.sendEmail(requestor.getEmail(),title,text);
                   }

               }break;
               default:{//其他
                   modelMap.put("success", false);
                   modelMap.put("Msg", "出库单审核失败，没有传入审核结果！");
                   log.error("出库审核传入状态不对状态为：",outStorageModel.getExeState());
               }break;
               }
               outStorageRepository.save(outStorage);

           }
         else {
             modelMap.put("success", false);
             modelMap.put("Msg", "没有出库单需要审核");
         }
       }
       else {
           modelMap.put("success", false);
           modelMap.put("Msg", "没有出库单需要审核");
       }
       return modelMap;
    }
    /**
     * 审批出库（高管操作）
     * 1.传入参数outStorageId，approver,approvalComments,exeState,histories
     * 2.完成出库单审批操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> seniorExecutiveApproval(OutStorageModel outStorageModel,Principal principal) {
        Map<String,Object>modelMap=new HashMap<>();
        String title="出库单审批！";
        String text= "出库单审批通过，请库房出库一下！";
        User user=userRepository.findByLoginName(principal.getName());
        if(outStorageModel.getOutStorageId()!=null){
                OutStorage outStorage=outStorageRepository.getOne(outStorageModel.getOutStorageId());
                outStorage.setHistories(outStorage.getHistories()+outStorageModel.getHistories());
                outStorage.setApproveDate(LocalDate.now());
                outStorage.setApproveComments(outStorageModel.getApproveComments());
                outStorage.setExeStat(outStorageModel.getExeState());
                switch (outStorageModel.getExeState()){
                    case 0:{
                        log.info(user.getLoginName()+"出库审批为待修改 "+outStorageModel.getRemark());
                        modelMap.put("success", true);
                        modelMap.put("Msg", "出库单审批成功");
                        outStorage.setExaminer("");
                        outStorage.setApprover("");
                        modelMap.put("OutStorage", outStorage);
                        User requestor=userRepository.findByLoginName(outStorage.getRequestor());
                        text="审批的出库单需要修改，详情请看出库申请备注！";
                        if(requestor!=null&&requestor.getEmail()!=null){
                            text=user.getLoginName()+text ;
                           mailService.sendEmail(requestor.getEmail(),title,text);
                        }
                    }break;
                    case 3:{
                        log.info(user.getLoginName()+"出库审批为同意（待出库）");
                        modelMap.put("success", true);
                        modelMap.put("Msg", "出库单审批成功");
                        modelMap.put("OutStorage", outStorage);
                    title="出库单审批！";
                    text="审批出库单通过，请库房出库一下！";
                    User auditor=userRepository.findByLoginName(outStorageModel.getAuditor());
                        if(auditor!=null&&auditor.getEmail()!=null){
                          text=user.getLoginName()+text;
                          mailService.sendEmail(auditor.getEmail(),title,text);
                        }
                }break;
                case 4:{
                        log.info(user.getLoginName()+"出库审批为待采购");
                        modelMap.put("success", true);
                        modelMap.put("Msg", "出库单审批成功");
                        modelMap.put("OutStorage", outStorage);
                    title="出库单审批！";
                    text="审批出库单为等待，库房物料不足，需要采购购买一下！";
                    List<Role> roles=new ArrayList<>();
                    Role role=roleRepository.findByRole("ROLE_BUYER");
                    roles.add(role);
                    List<User> users=userRepository.findByRoles(roles);
                    if(!users.isEmpty()){
                        for (User auditor:users) {
                            if(auditor.getEmail()!=null) {
                                text = user.getLoginName() + text ;
                                mailService.sendEmail(auditor.getEmail(), title, text);
                            }
                        }
                    }
                }break;
                case 7:{
                        log.info(user.getLoginName()+"出库审批为关闭");
                        modelMap.put("success", true);
                        modelMap.put("Msg", "出库单审批成功");
                        modelMap.put("OutStorage", outStorage);
                        outStorage.setCloseReason("审批拒绝！");
                    User requestor=userRepository.findByLoginName(outStorage.getRequestor());
                    text="审批出库单申请关闭，详情请看出库申请备注！";
                    if(requestor!=null&&requestor.getEmail()!=null){
                        text = user.getLoginName() + text ;
                        mailService.sendEmail(requestor.getEmail(),title,text);
                    }
                }break;
                default:{
                    log.error("传入状态不对，传入的状态为：",outStorageModel.getExeState());
                    modelMap.put("success", false);
                    modelMap.put("Msg", "没有出库单需要审批");
                }break;
                }
                outStorageRepository.save(outStorage);
            }
        else {
            modelMap.put("success", false);
            modelMap.put("Msg", "没有出库单需要审批");
        }
        return modelMap;
    }
    /**保存出库单（库管人员）
     * 1.传入参数outStorageModel
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> saveOutStorageKeeper(OutStorageModel outStorageModel,Principal principal) {
        Map<String, Object>modelMap=new HashMap<>();
        OutStoreKeeper outStoreKeeper=modelToOutStoreKeeper(outStorageModel);
        if(!StringUtils.isEmpty(outStoreKeeper)){
            List<DetailOutStoreKeeper>detailOutStoreKeepers=outStoreKeeper.getDetailOutStoreKeepers();
            outStoreKeeperRepository.save(outStoreKeeper);
            if(!detailOutStoreKeepers.isEmpty()){
                for (DetailOutStoreKeeper detailOutStoreKeeper:detailOutStoreKeepers) {
                    detailOutStoreKeeper.setOutStoreKeeper(outStoreKeeper);
                }

                modelMap.put("success", true);
                modelMap.put("Msg", "出库单保存成功！");
            }else {
                modelMap.put("success", false);
                modelMap.put("Msg", "没有出库单，请传入出库单！");
            }

        }else {
            modelMap.put("success", false);
            modelMap.put("Msg", "没有出库单，请传入出库单！");
        }
        return modelMap;
    }
    /**
     * 出库操作（库管人员）
     * 1.传入参数outStorageModel，exeState,histories
     * 2.完成出库操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> approvalOutStorage(OutStorageModel outStorageModel,Principal principal) {
        String remark=outStorageModel.getRemark();
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String title="出库单已成功出库！";
        String text= "修改出库单为成功出库，请确认出库一下！";
        Map<String,Integer>checkResult=checkOutStorage(outStorageModel);
        if(checkResult.isEmpty()) {
            User user = userRepository.findByLoginName(principal.getName());
            List<Role> roles = new ArrayList<>();
            Role role = roleRepository.findByRole("ROLE_STOREHOUSE");
            roles.add(role);
            List<User> storeMans = userRepository.findByRoles(roles);
            if (outStorageModel.getOutStorageId() != null) {
                OutStorage outStorage = outStorageRepository.getOne(outStorageModel.getOutStorageId());
                OutStoreKeeper outStoreKeeper = new OutStoreKeeper();
                if (outStorageModel.getOutStoreKeeperId() != null && outStoreKeeperRepository.existsById(outStorageModel.getOutStoreKeeperId())) {
                    outStoreKeeper = outStoreKeeperRepository.getOne(outStorageModel.getOutStoreKeeperId());
                } else {
                    outStoreKeeper = modelToOutStoreKeeper(outStorageModel);
                    outStoreKeeper.setAuditor(userRepository.findByLoginName(principal.getName()));
                }
                outStoreKeeper.setOutTime(LocalDate.now());
                List<DetailOutStorage> detailOutStorages = outStorage.getDetailOutStorages();
                outStoreKeeperRepository.save(outStoreKeeper);
                List<DetailOutStoreKeeper> detailOutStoreKeepers = outStoreKeeper.getDetailOutStoreKeepers();
                if (!detailOutStorages.isEmpty() && !detailOutStoreKeepers.isEmpty()) {
                    Float totalPrice=0f;
                    for (DetailOutStoreKeeper detailOutStoreKeeper : detailOutStoreKeepers) {
                        detailOutStoreKeeper.setOutStoreKeeper(outStoreKeeper);
                        Storage storage = storageRepository.findByMaterialAndSequence(detailOutStoreKeeper.getMaterial(), detailOutStoreKeeper.getBatchNumber());
                        if(storage.getAmount()!=null&&detailOutStoreKeeper.getNumber()!=null)
                       //如果库存数量够，执行出库操作
                        if (!StringUtils.isEmpty(storage) && storage.getAmount() >= detailOutStoreKeeper.getNumber()) {
                            log.info("库存数量足够，"+user.getLoginName()+"执行出库操作");
                            outStorage.setExeStat(6);
                            outStorage.setHistories(outStorage.getHistories() + outStorageModel.getHistories());
                            outStorage.setAuditor(principal.getName());
                            outStorage.setRemark(outStorage.getRemark() + remark);
                            outStorage.setOutTime(LocalDate.now());
                            if(storage.getPrice()!=null&&detailOutStoreKeeper.getNumber()!=null)
                                totalPrice=totalPrice+storage.getPrice()*detailOutStoreKeeper.getNumber();
                            storage.setAmount(storage.getAmount() - detailOutStoreKeeper.getNumber());
                            modelMap.put("success", true);
                            modelMap.put("Msg", "出库成功");
                            outStorage.setAuditor(principal.getName());
                            modelMap.put("OutStorage", outStorage);
                            outStorageRepository.save(outStorage);
                            storageRepository.save(storage);
                            if (storage.getAmount() == 0) {
                                storageRepository.delete(storage);
                            }
                            if (!storeMans.isEmpty()) {
                                for (User storeMan : storeMans) {
                                    if (storeMan.getEmail() != null) {
                                        text = user.getLoginName() + text ;
                                        mailService.sendEmail(storeMan.getEmail(), title, text);
                                    }
                                }
                            } else {
                                title = "出库单审批！";
                                text = "没有找到库管角色的人，请通知管理员建立管理员！";
                                if (user.getEmail() != null) {
                                    text = user.getLoginName() + text ;
                                    mailService.sendEmail(user.getEmail(), title, text);
                                }
                            }
                        } //所选物料的批次的库存数量不够
                        else {
                            log.error("所选批次的数量库存不够，传入数量不对");
                            modelMap.put("success", false);
                            modelMap.put("Msg", "出库数量大于物料数量，请重新选择！");
                        }
                    }
                    outStorage.setTotalPrice(totalPrice);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("Msg", "没有出库单，请传入出库单！");
                }
            }
            else {
                log.error("传入的出库单为空！");
                modelMap.put("success", false);
                modelMap.put("Msg", "出库单为空，请选择出库单");
            }
        }else {
                log.error("校验的数量不对等");
                modelMap.put("Msg","入库失败，校验未通过，对应的物料和数量在map中！");
                modelMap.put("checkResult",checkResult);

        }
        return modelMap;
    }
    /**
     * 确认出库（开发人员）
     * 1.传入参数outStorageId
     * 2.完成出库确认操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> confirmOutStorage(OutStorageModel outStorageModel, Principal principal) {
        Map<String,Object>modelMap=new HashMap<>();
        if(outStorageModel.getOutStorageId()!=null){
            OutStorage outStorage=outStorageRepository.getOne(outStorageModel.getOutStorageId());
            if(!StringUtils.isEmpty(outStorage)){
                 outStorage.setExeStat(7);
                 outStorage.setCloseReason("完成！");
                 outStorageRepository.save(outStorage);
                 modelMap.put("success", true);
                 modelMap.put("Msg", "出库单出库确认成功！");
                 modelMap.put("OutStorage", outStorage);
            }
        }
        else {
            modelMap.put("success", false);
            modelMap.put("Msg", "没有出库单需要出库确认！");
        }
        return modelMap;
    }
    /**
     * 查出所有的出库清单
     * 1.查出所有的出库申请
     * 2.传入参数为空
     * @return
     */
    @Override
    public Map<String, Object> findAllOutStorage() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<OutStorage> outStorages=outStorageRepository.findAll();
        List<OutStorageModel> outStorageModels=new ArrayList<>();
        if(!StringUtils.isEmpty(outStorages)){
            for (OutStorage outStorage:outStorages) {
                outStorageModels.add(outStorageToModel(outStorage));
            }
            modelMap.put("success",true);
            modelMap.put("Msg","获取出库列表成功");
            modelMap.put("outStorageModels",outStorageModels);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","出库记录为空，获取出库记录失败");
        }
        return modelMap;
    }
    /**通过传入的参数条件查找出库申请单（部门经理或者高管调用）
     * 1.传入参数type,department,programNumber,requestor,startTime,endTime
     * @param type
     * @param department
     * @param programNumber
     * @param requestor
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String, Object> findAllOutStorageByCondition(Integer type, String department, String programNumber, String requestor, LocalDate startTime, LocalDate endTime) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<OutStorage> outStorages=outStorageRepository.find(type,department,programNumber,requestor,startTime,endTime);
        List<OutStorageModel> outStorageModels=new ArrayList<>();
        if(!StringUtils.isEmpty(outStorages)){
            for (OutStorage outStorage:outStorages) {
                outStorageModels.add(outStorageToModel(outStorage));
            }
            modelMap.put("success",true);
            modelMap.put("Msg","获取出库列表成功");
            modelMap.put("outStorageModels",outStorageModels);
        }else {
            modelMap.put("success",false);
            modelMap.put("Msg","出库记录为空，获取出库记录失败");
        }
        return modelMap;
    }
    /**通过id查询出库单
     * 1.传入参数outStorageId
     * 2.通过传入的id查询出库单
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findOutStorageById(Integer id) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(outStorageRepository.existsById(id)){
            OutStorage outStorage=outStorageRepository.getOne(id);
            OutStorageModel outStorageModel=outStorageToModel(outStorage);
            modelMap.put("success",true);
            modelMap.put("Msg","通过id获取入库记录成功");
            modelMap.put("outStorageModel",outStorageModel);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","没有该入库记录，获取入库记录失败");
        }
        return modelMap;
    }
    /**
     * 待修改和提交查询（由开发人员调用当前接口）
     * 1.实现查询我已经保存但是没有提交的出库申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyApplyOutStorage(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
           User user=userRepository.findByLoginName(principal.getName());
           List<OutStorage> outStorages=outStorageRepository.findByRequestorAndExeStat(user.getLoginName(),0);
           List<OutStorageModel> myApplyOutStorages=new ArrayList<>();
           if(!outStorages.isEmpty()){
               for (OutStorage outStorage:outStorages) {
                   OutStorageModel outStorageModel=outStorageToModel(outStorage);
                   myApplyOutStorages.add(outStorageModel);
               }
               modelMap.put("MyApplyOutStorage",myApplyOutStorages);
           }else {
               modelMap.put("Msg","我未提交的出库申请为空！");
           }
        List<OutStorage> historyOutStorages=outStorageRepository.findByRequestorOrderByExeStat(user.getLoginName());
        List<OutStorageModel> myHistoryOutStorages=new ArrayList<>();
        if(!historyOutStorages.isEmpty()){
            for (OutStorage outStorage:historyOutStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                myHistoryOutStorages.add(outStorageModel);
            }
            modelMap.put("myHistoryOutStorages",myHistoryOutStorages);
        }
        else {
            modelMap.put("Msg","我的历史出库申请为空！");
        }

        return modelMap;
    }
    /**
     * 待审核查询（由部门经理调用当前接口）
     * 1.实现查找我能审核的出库申请
     * 2.实现查找我历史审核的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyApprovalOutStorage(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        User user=userRepository.findByLoginName(principal.getName());
        List<OutStorage>outStorages=outStorageRepository.findByDepartmentAndExeStat(user.getOperation(),1);
        List<OutStorageModel> myApprovalOutStorages=new ArrayList<>();
        if(!outStorages.isEmpty()){
            for (OutStorage outStorage:outStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                myApprovalOutStorages.add(outStorageModel);
            }
            modelMap.put("MyApprovalOutStorage",myApprovalOutStorages);
        }else {
            modelMap.put("Msg","我要审核的出库申请为空！");
        }
        List<OutStorage>historyOutStorages=outStorageRepository.findByExaminer(user.getLoginName());
        List<OutStorageModel> myHistoryApprovalOutStorages=new ArrayList<>();
        if(!historyOutStorages.isEmpty()){
            for (OutStorage outStorage:historyOutStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                myHistoryApprovalOutStorages.add(outStorageModel);
            }
            modelMap.put("MyHistoryApprovalOutStorage",myHistoryApprovalOutStorages);
        }else {
            modelMap.put("Msg","我的出库审核历史为空！");
        }
        return modelMap;
    }
    /**
     * 待审批查询（由高管调用当前接口）
     * 1.实现查找我能审批的出库申请
     * 2.实现查找我历史审批的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findHighApprovalOutStorage(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        User user=userRepository.findByLoginName(principal.getName());
        List<OutStorage>outStorages=outStorageRepository.findByApproverAndExeStat(user.getLoginName(),2);
        List<OutStorageModel> myHighApprovalOutStorages=new ArrayList<>();
        if(!outStorages.isEmpty()){
            for (OutStorage outStorage:outStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                myHighApprovalOutStorages.add(outStorageModel);
            }
            modelMap.put("MyHighApprovalOutStorage",myHighApprovalOutStorages);
        }else {
            modelMap.put("Msg","我要审批的出库申请为空！");
        }
        List<Integer> exeStates=new ArrayList<>();
        exeStates.add(1);
        exeStates.add(0);
        exeStates.add(3);
        exeStates.add(4);
        exeStates.add(5);
        exeStates.add(7);
        List<OutStorage> historyHighApprovalOutStorages=outStorageRepository.findByApproverAndExeStatIn(user.getLoginName(),exeStates);
        List<OutStorageModel> myHistoryHighApprovalOutStorages=new ArrayList<>();
        if(!historyHighApprovalOutStorages.isEmpty()){
            for (OutStorage outStorage:historyHighApprovalOutStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                myHistoryHighApprovalOutStorages.add(outStorageModel);
            }
            modelMap.put("myHistoryHighApprovalOutStorages",myHistoryHighApprovalOutStorages);
        }
        else {
            modelMap.put("Msg","我的历史出库审批为空！");
        }
        return modelMap;
    }
    /**待出库查询
     * 1.查询所有的待出库申请记录
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyOutStorage(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<Integer>exeStates=new ArrayList<>();
        exeStates.add(8);
        exeStates.add(3);
        List<OutStorage> outStorages=outStorageRepository.findByExeStatIn(exeStates);
        List<OutStorageModel> MyOutStorage=new ArrayList<>();
        if(!outStorages.isEmpty()){
            for (OutStorage outStorage:outStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                MyOutStorage.add(outStorageModel);
            }
            modelMap.put("MyOutStorage",MyOutStorage);
        }else {
            modelMap.put("Msg","我要出库的申请为空！");
        }
        List<OutStorage>historyOutStorages=outStorageRepository.findByAuditor(principal.getName());
        List<OutStorageModel> MyHistoryOutStorage=new ArrayList<>();
        if(!historyOutStorages.isEmpty()){
            for (OutStorage outStorage:historyOutStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                MyHistoryOutStorage.add(outStorageModel);
            }
            modelMap.put("MyHistoryOutStorage",MyHistoryOutStorage);
        }else {
            modelMap.put("Msg","我的历史出库为空！");
        }
        return modelMap;
    }
    /**
     * 采购人员查询等待的出库单（等待状态查询）
     * 1.传入参数为空
     * 2.查询采购人员需要采购的出库单
     * @param principal
     * @return
     */
    @Override
    public Map<String, Object> findMyOutStorageToPurchase(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<OutStorage>outStorages=outStorageRepository.findByExeStat(4);
        List<OutStorageModel> MyOutStorageToPurchase=new ArrayList<>();
        if(!outStorages.isEmpty()){
            for (OutStorage outStorage:outStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                MyOutStorageToPurchase.add(outStorageModel);
            }
            modelMap.put("MyOutStorageToPurchase",MyOutStorageToPurchase);
        }else {
            modelMap.put("Msg","我要采购的出库申请为空！");
        }
        return modelMap;
    }
    /**
     * 申请人员查询出库完成的出库单
     * 1.传入参数为空
     * 2.完成查询出库完成的出库单的操作
     * @param
     * @return
     */
    @Override
    public Map<String, Object> findMyOutStorageToConfirm(Principal principal) {
        Map<String, Object> modelMap=new HashMap<>();
        List<OutStorage>outStorages=outStorageRepository.findByRequestorAndExeStat(principal.getName(),6);
        List<OutStorageModel> outStorageModels=new ArrayList<>();
        if(!outStorages.isEmpty()){
            for (OutStorage outStorage:outStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                outStorageModels.add(outStorageModel);
                modelMap.put("MyOutStorageToConfirm",outStorageModels);
            }
        }else {
            modelMap.put("Msg","出库完成需要待确认的出库申请单为空！");
        }
        List<OutStorage>historyOutStorages=outStorageRepository.findByRequestorAndExeStat(principal.getName(),7);
        List<OutStorageModel> historyOutStorageModels=new ArrayList<>();
        if(!historyOutStorages.isEmpty()){
            for (OutStorage outStorage:historyOutStorages) {
                OutStorageModel outStorageModel=outStorageToModel(outStorage);
                historyOutStorageModels.add(outStorageModel);
                modelMap.put("MyHistoryOutStorageToConfirm",historyOutStorageModels);
            }
        }else {
            modelMap.put("Msg","历史出库关闭的出库单为空！");
        }
        return modelMap;
    }
    /**通过传入的outStorageId查询OutStoreKeeper
     * 1.传入参数outStorageId
     * @param outStorageId
     * @return
     */
    @Override
    public Map<String, Object> findOutStorageKeeperByOutStorageId(Integer outStorageId) {
        Map<String, Object> modelMap=new HashMap<>();
        if(outStorageRepository.existsById(outStorageId)){
            OutStorage outStorage=outStorageRepository.getOne(outStorageId);
            List<OutStoreKeeper>outStoreKeepers=outStoreKeeperRepository.findByOutStorage(outStorage);
            if(!outStoreKeepers.isEmpty()){
                modelMap.put("Msg","查询保存的出库单成功！");
                modelMap.put("outStoreKeepers",outStoreKeepers);
            }else {
                modelMap.put("Msg","出库单为空！");
            }
        }else {
            modelMap.put("Msg","传入的出库单id为空！");
        }
        return modelMap;
    }

    /**查询所有的出库单（库管填写的出库单）
     * @return
     */
    @Override
    public Map<String, Object> findAllOutStorageKeeper() {
        Map<String, Object> modelMap=new HashMap<>();
            List<OutStoreKeeper>outStoreKeepers=outStoreKeeperRepository.findAll();
            if(!outStoreKeepers.isEmpty()){
                List<OutStorageModel>outStorageModels=new ArrayList<>();
                for (OutStoreKeeper outStoreKeeper:outStoreKeepers) {
                   OutStorageModel outStorageModel=new OutStorageModel();
                    if(outStoreKeeper!=null){
                        outStorageModel=outStoreKeeperToOutStorageModel(outStoreKeeper);
                        outStorageModels.add(outStorageModel);
                    }
                }
                modelMap.put("Msg","查询保存的出库单成功！");
                modelMap.put("outStoreKeepers",outStorageModels);
            }else {
                modelMap.put("Msg","出库单为空！");
            }
        return modelMap;
    }
    /**
     * 将model转化为出库申请清单
     * @param outStorageModel
     * @return
     */
    @Override
    public OutStorage modelToOutStorage(OutStorageModel outStorageModel){
        OutStorage outStorage =new OutStorage();
//        Integer i=0;
        outStorage.setExeStat(outStorageModel.getExeState());
        outStorage.setRequestNo(outStorageModel.getRequestNo());
        outStorage.setCreateDate(outStorageModel.getCreateDate());
        outStorage.setDepartment(outStorageModel.getDepartment());
        outStorage.setRequestor(outStorageModel.getRequestor());
        List<DetailOutStorage> detailOutStorages=new ArrayList<>();
        if(outStorageModel.getOutStorageId()!=null&&outStorageRepository.existsById(outStorageModel.getOutStorageId())){
            outStorage=outStorageRepository.getOne(outStorageModel.getOutStorageId());
            detailOutStorages=outStorage.getDetailOutStorages();
        }
//        Map<Integer,Integer> detailOutStorageIds=new HashMap<>();
        List<DetailOutStorage>deleteDetailOutStorage=new ArrayList<>();
        Set<DetailStorageModel> detailStorageModels=outStorageModel.getOutMaterial();
        if(!StringUtils.isEmpty(detailStorageModels)){
            if(!detailOutStorages.isEmpty()){
                //通过遍历找出新的申请单里面没有但是老的申请单里面有的物料并添加到待删除列表

                for (DetailOutStorage detailOutStorage:detailOutStorages) {
                    boolean flag=true;
                    for (DetailStorageModel detailStorageModel:detailStorageModels) {
                        if(detailOutStorage.getMaterial().getMaterialId()==detailStorageModel.getMaterialId()){
                            flag=false;
                        }
                    }
                    if(flag){
//                       ++i;
//                       detailOutStorageIds.put(i,detailOutStorage.getDetailOutStorageId());
                       deleteDetailOutStorage.add(detailOutStorage);
                    }
                }
            }
            //如果删除列表不为空，在原来的列表里面删除物料
            if(!deleteDetailOutStorage.isEmpty()){
                detailOutStorages.removeAll(deleteDetailOutStorage);
                for (DetailOutStorage deleteOutStorage:deleteDetailOutStorage) {
                    detailOutStorageRepository.delete(deleteOutStorage);
                }
            }
            //通过遍历查找出原来申请单里有的物料并修改，原来申请单里面没有的物料添加进去
            for (DetailStorageModel detailStorageModel : detailStorageModels) {
                if(detailStorageModel.getDetailOutStorageId()!=null&&detailOutStorageRepository.existsById(detailStorageModel.getDetailOutStorageId())){
                    for (DetailOutStorage detailOutStorage:detailOutStorages) {
                        if (detailStorageModel.getDetailOutStorageId() == detailOutStorage.getDetailOutStorageId()) {
                            detailOutStorage.setNumber(detailStorageModel.getAmount());
                            detailOutStorage.setProgramNumber(detailStorageModel.getProgramNumber());
                        }
                    }
                }else {
                    DetailOutStorage detailOutStorage = new DetailOutStorage();
                    detailOutStorage.setNumber(detailStorageModel.getAmount());
                    detailOutStorage.setProgramNumber(outStorageModel.getProgramNumber());
                    Material material=materialRepository.getOne(detailStorageModel.getMaterialId());
                    material.setMiniumTemp(detailStorageModel.getMiniumTemp());
                    material.setMaxTemp(detailStorageModel.getMaxTemp());
                    material.setEncapsulation(detailStorageModel.getEncapsulation());
                    detailOutStorage.setMaterial(material);
                    detailOutStorages.add(detailOutStorage);
                }
            }}
        outStorage.setProgramNumber(outStorageModel.getProgramNumber());
        outStorage.setName(outStorageModel.getName());
        outStorage.setAuditor(outStorageModel.getAuditor());
        outStorage.setRemark(outStorageModel.getRemark());
        outStorage.setStoreageName(outStorageModel.getStoreageName());
        outStorage.setType(outStorageModel.getType());
        outStorage.setBrderNumber(outStorageModel.getBrderNumber());
        outStorage.setExaminer(outStorageModel.getExaminer());
        outStorage.setApprover(outStorageModel.getApprover());
        outStorage.setAuditDate(outStorageModel.getAuditDate());
        outStorage.setApproveDate(outStorageModel.getApproveDate());
        outStorage.setHistories(outStorageModel.getHistories());
        outStorage.setAuditComments(outStorageModel.getAuditComments());
        outStorage.setApproveComments(outStorageModel.getApproveComments());
        outStorage.setDetailOutStorages(detailOutStorages);
        return outStorage;
    }
    /**
     * 将出库申请清单转换为model
     * @param outStorage
     * @return
     */
    @Override
    public OutStorageModel outStorageToModel(OutStorage outStorage) {
       OutStorageModel outStorageModel=new OutStorageModel();
        String name="";
        float totalPrice=0;
        int totalNumber=0;
        int typeNum=0;
        Set<DetailStorageModel> detailStorageModels=new HashSet<>();
        List<DetailOutStorage> detailOutStorages=outStorage.getDetailOutStorages();
        if(!StringUtils.isEmpty(detailOutStorages)){
        for (DetailOutStorage detailOutStorage:detailOutStorages) {
            DetailStorageModel detailStorageModel=new DetailStorageModel();
            name=name+detailOutStorage.getMaterial().getName()+",";
            totalNumber=totalNumber+detailOutStorage.getNumber();
            detailStorageModel.setDetailOutStorageId(detailOutStorage.getDetailOutStorageId());
            detailStorageModel.setProgramNumber(detailOutStorage.getProgramNumber());//项目号
            detailStorageModel.setNumber(detailOutStorage.getNumber());
            Material material=detailOutStorage.getMaterial();
            if(!StringUtils.isEmpty(material)){
                int amount=0;
                detailStorageModel.setName(material.getName());
                detailStorageModel.setModelNumber(material.getModelNumber());
                detailStorageModel.setCode(material.getCode());
                detailStorageModel.setRemark(material.getRemark());
                detailStorageModel.setMaterialId(material.getMaterialId());
                detailStorageModel.setMiniumTemp(material.getMiniumTemp());
                detailStorageModel.setMaxTemp(material.getMaxTemp());
                detailStorageModel.setEncapsulation(material.getEncapsulation());
                if(material.getType()!=null)
                detailStorageModel.setMaterialType(material.getType().getTypeName());
                List<Storage> storages=storageRepository.findByMaterial(material);
                if(!storages.isEmpty()){
                    for (Storage storage:storages) {
                        amount=amount+storage.getAmount();
                    }
                    detailStorageModel.setAmount(amount);
                }else {
                    detailStorageModel.setAmount(0);
                }
            }

            typeNum=typeNum+1;
            detailStorageModels.add(detailStorageModel);
        }
        }
        if(!StringUtils.isEmpty(name))
        name=name.substring(0,name.length()-1);
        outStorageModel.setTypeNum(typeNum);
        outStorageModel.setProgramNumber(outStorage.getProgramNumber());
        outStorageModel.setName(outStorage.getName());
        outStorageModel.setRequestNo(outStorage.getRequestNo());//申请单号
        outStorageModel.setAuditor(outStorage.getAuditor());//出库人
        outStorageModel.setRequestor(outStorage.getRequestor());//领用人
        outStorageModel.setRemark(outStorage.getRemark());  //备注
        outStorageModel.setExeState(outStorage.getExeStat());//状态
        outStorageModel.setTotalPrice(totalPrice);//总金额
        outStorageModel.setOutStorageProduct(name);//出库物料名称
        outStorageModel.setOutStorageId(outStorage.getOutStorageId()); //出库单id
        outStorageModel.setType(outStorage.getType()); //出库类型
        outStorageModel.setStoreageName(outStorage.getStoreageName());//仓库名称
        outStorageModel.setDepartment(outStorage.getDepartment());//所属部门
        outStorageModel.setBrderNumber(outStorage.getBrderNumber());//订单号
        outStorageModel.setExaminer(outStorage.getExaminer());//审核人
        outStorageModel.setApprover(outStorage.getApprover());//审批人
        outStorageModel.setCreateDate(outStorage.getCreateDate());//创建日期
        outStorageModel.setAuditDate(outStorage.getAuditDate());//审核日期
        outStorageModel.setApproveDate(outStorage.getApproveDate());//审批日期
        outStorageModel.setHistories(outStorage.getHistories());//状态变化历史
        outStorageModel.setAuditComments(outStorage.getAuditComments());
        outStorageModel.setApproveComments(outStorage.getApproveComments());
        outStorageModel.setOutMaterial(detailStorageModels);
        outStorageModel.setTotalNumber(totalNumber);
        outStorageModel.setOutTime(outStorage.getOutTime());

       return outStorageModel;
    }

    /**将model转化为出库清单
     * @param outStorageModel
     * @return
     */
    @Override
    public OutStoreKeeper modelToOutStoreKeeper(OutStorageModel outStorageModel) {
        OutStoreKeeper outStoreKeeper=new OutStoreKeeper();
        outStoreKeeper.setAuditor(userRepository.findByLoginName(outStorageModel.getAuditor()));
        outStoreKeeper.setRequestor(userRepository.findByLoginName(outStorageModel.getRequestor()));
        outStoreKeeper.setName(outStorageModel.getName());
        outStoreKeeper.setRequestNo(outStorageModel.getRequestNo());
        outStoreKeeper.setType(outStorageModel.getType());
        outStoreKeeper.setStoreageName(outStorageModel.getStoreageName());
        outStoreKeeper.setDepartment(outStorageModel.getDepartment());
        outStoreKeeper.setRemark(outStorageModel.getRemark());
        outStoreKeeper.setOutStorageNo(outStorageModel.getOutStorageNo());
        outStoreKeeper.setProgramNumber(outStorageModel.getProgramNumber());
        outStoreKeeper.setOutStorage(outStorageRepository.getOne(outStorageModel.getOutStorageId()));
        List<DetailOutStoreKeeper>detailOutStoreKeepers=new ArrayList<>();
        Set<DetailStorageModel>detailStorageModels=outStorageModel.getOutMaterial();
        if(!detailStorageModels.isEmpty()){
            for (DetailStorageModel detailStorageModel:detailStorageModels) {
                DetailOutStoreKeeper detailOutStoreKeeper=new DetailOutStoreKeeper();
                detailOutStoreKeeper.setPrice(detailStorageModel.getPrice());
                detailOutStoreKeeper.setNumber(detailStorageModel.getNumber());
                detailOutStoreKeeper.setBatchNumber(detailStorageModel.getSequence());
                detailOutStoreKeeper.setProgramNumber(outStorageModel.getProgramNumber());
                detailOutStoreKeeper.setOutStoreKeeper(outStoreKeeper);
                Material material=materialRepository.getOne(detailStorageModel.getMaterialId());
                detailOutStoreKeeper.setMaterial(material);
                detailOutStoreKeepers.add(detailOutStoreKeeper);
            }
        }
        outStoreKeeper.setDetailOutStoreKeepers(detailOutStoreKeepers);
        return outStoreKeeper;
    }

    /**出库单验证
     * @param outStorageModel
     * @return
     */
    private Map<String,Integer> checkOutStorage(OutStorageModel outStorageModel) {
        Map<String, Integer> modelMap = new HashMap<>();
        if (outStorageModel.getOutStorageId() != null) {
            int account = 0;
            OutStorage outStorage = outStorageRepository.getOne(outStorageModel.getOutStorageId());
            OutStoreKeeper outStoreKeeper = modelToOutStoreKeeper(outStorageModel);
            List<DetailOutStorage> detailOutStorages = outStorage.getDetailOutStorages();
            List<DetailOutStoreKeeper> detailOutStoreKeepers = outStoreKeeper.getDetailOutStoreKeepers();
            if (detailOutStorages != null && !detailOutStorages.isEmpty() && detailOutStoreKeepers != null && !detailOutStoreKeepers.isEmpty())
                for (DetailOutStorage detailOutStorage : detailOutStorages) {
                    account=0;
                    account = detailOutStorage.getNumber();
                    for (DetailOutStoreKeeper detailOutStoreKeeper : detailOutStoreKeepers) {
                        if (detailOutStorage.getMaterial().getMaterialId() == detailOutStoreKeeper.getMaterial().getMaterialId()) {
                            account = account - detailOutStoreKeeper.getNumber();
                        }
                    }
                    if (account != 0){
                        String name=detailOutStorage.getMaterial().getName();
                        modelMap.put(name, account);
                    }
                }
        }
        return modelMap;
    }

    /**将outStoreKeeper转换为Model
     * @param outStoreKeeper
     * @return
     */
    private OutStorageModel outStoreKeeperToOutStorageModel(OutStoreKeeper outStoreKeeper){
        OutStorageModel outStorageModel=new OutStorageModel();
        int totalNumber=0;
        int typeNum=0;
        Float totalPrice=0.0f;
        outStorageModel.setProgramNumber(outStoreKeeper.getProgramNumber());
        outStorageModel.setName(outStoreKeeper.getName());
        outStorageModel.setRequestNo(outStoreKeeper.getRequestNo());
        outStorageModel.setOutStorageNo(outStoreKeeper.getOutStorageNo());
        if(outStoreKeeper.getAuditor()!=null)
        outStorageModel.setAuditor(outStoreKeeper.getAuditor().getLoginName());
        if(outStoreKeeper.getRequestor()!=null)
        outStorageModel.setRequestor(outStoreKeeper.getRequestor().getLoginName());
        outStorageModel.setRemark(outStoreKeeper.getRemark());
        outStorageModel.setOutTime(outStoreKeeper.getOutTime());
        outStorageModel.setType(outStoreKeeper.getType());
        if(outStoreKeeper.getOutStorage()!=null){
            outStorageModel.setOutStorageId(outStoreKeeper.getOutStorage().getOutStorageId());
        }
        outStorageModel.setOutStoreKeeperId(outStoreKeeper.getId());
        outStorageModel.setStoreageName(outStoreKeeper.getStoreageName());
        outStorageModel.setDepartment(outStoreKeeper.getDepartment());
        Set<DetailStorageModel>detailStorageModels=new HashSet<>();
        List<DetailOutStoreKeeper>detailOutStoreKeepers=outStoreKeeper.getDetailOutStoreKeepers();
        if(!detailOutStoreKeepers.isEmpty())
        for (DetailOutStoreKeeper detailOutStoreKeeper:detailOutStoreKeepers) {
            ++typeNum;
            DetailStorageModel detailStorageModel=new DetailStorageModel();
            detailStorageModel.setSequence(detailOutStoreKeeper.getBatchNumber());
            detailStorageModel.setProgramNumber(detailOutStoreKeeper.getProgramNumber());
            if(detailOutStoreKeeper.getNumber()!=null){
                totalNumber=totalNumber+detailOutStoreKeeper.getNumber();
                detailStorageModel.setNumber(detailOutStoreKeeper.getNumber());
            }
            if(detailOutStoreKeeper.getNumber()!=null&&detailOutStoreKeeper.getPrice()!=null){
                totalPrice=totalPrice+detailOutStoreKeeper.getNumber()*detailOutStoreKeeper.getPrice();
            }
            detailStorageModel.setPrice(detailOutStoreKeeper.getPrice());

            Material material=detailOutStoreKeeper.getMaterial();
            if(material!=null){
                Type type=material.getType();
                detailStorageModel.setName(material.getName());
                detailStorageModel.setModelNumber(material.getModelNumber());
                detailStorageModel.setCode(material.getCode());
                detailStorageModel.setRemark(material.getRemark());
                detailStorageModel.setMaterialId(material.getMaterialId());
                detailStorageModel.setMiniumTemp(material.getMiniumTemp());
                detailStorageModel.setMaxTemp(material.getMaxTemp());
                detailStorageModel.setEncapsulation(material.getEncapsulation());
                if(type!=null){
                    detailStorageModel.setMaterialType(type.getTypeName());
                }
            }
            detailStorageModels.add(detailStorageModel);
        }
        outStorageModel.setTypeNum(typeNum);
        outStorageModel.setTotalNumber(totalNumber);
        outStorageModel.setTotalPrice(totalPrice);
        outStorageModel.setOutMaterial(detailStorageModels);
        return outStorageModel;
    }
}
