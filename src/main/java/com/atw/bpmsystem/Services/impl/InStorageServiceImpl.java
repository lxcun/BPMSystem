package com.atw.bpmsystem.Services.impl;

import com.alibaba.fastjson.JSON;
import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.DetailStorageModel;
import com.atw.bpmsystem.Models.InStorageModel;
import com.atw.bpmsystem.Models.ListInStorageModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.InStorageService;
import com.atw.bpmsystem.Services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
@EnableTransactionManagement
@Slf4j
public class InStorageServiceImpl implements InStorageService {
    @Autowired
    private InStorageRepository inStorageRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private QualityAuditRepository qualityAuditRepository;
    @Autowired
    private OutStorageRepository outStorageRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    /**
     * 添加入库单
     * 1.传入参数，界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成添加入库单的操作
     * @param inStorageModel
     * @param principal
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addInStorage(InStorageModel inStorageModel,Principal principal) {
        InStorage inStorage = modelToInStorage(inStorageModel);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(inStorage)) {
            inStorage.setInStoreDate(LocalDate.now());
            inStorage.setDeliverer(principal.getName());
            inStorage.setDeliveryman("");
            inStorageRepository.save(inStorage);
            List<DetailInStorage> detailInStorages = inStorage.getDetailInStorages();
            for (DetailInStorage detailInStorage : detailInStorages) {
                detailInStorage.setInStorage(inStorage);
                Material material=detailInStorage.getMaterial();
                Storage storage=storageRepository.findByMaterialAndSequence(material,detailInStorage.getBatchNumber());
                if(!StringUtils.isEmpty(storage)){
                    storage.setAmount(storage.getAmount()+detailInStorage.getNumber());
                }else {
                    storage = new Storage();
                    storage.setMaterial(material);
                    storage.setAmount(detailInStorage.getNumber());
                    storage.setPrice(detailInStorage.getPrice());
                    storage.setSeller(sellerRepository.findBySellerName(detailInStorage.getSeller()));
                    storage.setProgramNumber(detailInStorage.getProgramNumber());
                    storage.setProduceFactoryName(detailInStorage.getProduceFactoryName());
                    storage.setSequence(detailInStorage.getBatchNumber());
                }
                storage.setStoreageName(inStorage.getStoreageName());
                storage.setEndDate(LocalDate.now());
                storageRepository.save(storage);
            }
            modelMap.put("success", true);
            modelMap.put("Msg", "入库成功");
            modelMap.put("InStorage", inStorage);
        } else {
            modelMap.put("success", false);
            modelMap.put("Msg", "入库失败，请输入入库物料");
        }
        return modelMap;
    }

    /**
     * 批量添加入库单
     * 1.传入参数，界面上所有的入库字段（参照InStorageModel和DetailStorageModel）Lis格式
     * 2.完成批量添加入库单的操作
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addInStorageList(ListInStorageModel listInStorageModel,Principal principal) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Map<String,Integer> checkResult=checkInStorage(listInStorageModel);
        if(checkResult.isEmpty()) {
            Map<Integer,LocalDate>endDate=new HashMap<>();
            Map<Integer,LocalDate>productionDate=new HashMap<>();
            User deliverman=new User();
            List<Integer>qualityAuditIdList=listInStorageModel.getQualityAuditIdList();
            for (Integer qulityAuditId:qualityAuditIdList) {
                QualityAudit qualityAudit=qualityAuditRepository.getOne(qulityAuditId);
                qualityAudit.setState(2);
                List<DetailQualityAudit> detailQualityAudits=qualityAudit.getDetailQualityAudits();
                for (DetailQualityAudit detailQualityAudit:detailQualityAudits) {
                    DetailPurchase detailPurchase=detailQualityAudit.getDetailPurchase();
                    if(detailPurchase!=null){
                        Material material=detailPurchase.getMaterial();
                        if(material!=null){
                            endDate.put(material.getMaterialId(),detailQualityAudit.getEndDate());
                            productionDate.put(material.getMaterialId(),detailQualityAudit.getProductionDate());
                        }
                        Purchase purchase=detailPurchase.getPurchase();
                        DetailOutStorage detailOutStorage=detailPurchase.getDetailOutStorage();
                        if(purchase!=null){
                            purchase.setExeState(8);
                            purchase.setInStorageDate(LocalDate.now());
                            purchaseRepository.save(purchase);
                        }
                        deliverman =purchase.getRequestor();
                        if(detailOutStorage!=null){
                            OutStorage outStorage=detailOutStorage.getOutStorage();
                            outStorage.setExeStat(8);
                            outStorageRepository.save(outStorage);
                            if(outStorage.getRequestor()!=null){
                                User requestor=userRepository.findByLoginName(outStorage.getRequestor());
                                String text="你所提的物料申请已经采购回来入库房了，请来库房领取一下！";
                                String title="出库申请物料通知！";
                                if(requestor!=null&&requestor.getEmail()!=null)
                                    mailService.sendEmail(requestor.getEmail(),text,title);
                            }

                        }
                    }

                }

                qualityAuditRepository.save(qualityAudit);
            }
            List<InStorageModel> inStorageModels = listInStorageModel.getInStorageModels();
            for (InStorageModel inStorageModel:inStorageModels) {
                InStorage inStorage=modelToInStorage(inStorageModel);
                inStorage.setInStoreDate(LocalDate.now());
                inStorage.setDeliverer(principal.getName());
                if(deliverman!=null)
                inStorage.setDeliveryman(deliverman.getLoginName());
                log.info(principal.getName()+"提交了入库清单"+ JSON.toJSONString(inStorage));
                inStorageRepository.save(inStorage);
                if (!StringUtils.isEmpty(inStorage)) {
                    List<DetailInStorage> detailInStorages=inStorage.getDetailInStorages();
                    for (DetailInStorage detailInStorage : detailInStorages) {
                        detailInStorage.setInStorage(inStorage);
                        Material material=detailInStorage.getMaterial();
                        Storage storage=storageRepository.findByMaterialAndSequence(material,detailInStorage.getBatchNumber());
                        if(!StringUtils.isEmpty(storage)){
                            storage.setAmount(storage.getAmount()+detailInStorage.getNumber());
                        }else {
                            storage = new Storage();
                            storage.setMaterial(material);
                            storage.setAmount(detailInStorage.getNumber());
                            storage.setPrice(detailInStorage.getPrice());
                            storage.setSeller(sellerRepository.findBySellerName(detailInStorage.getSeller()));
                            storage.setProgramNumber(detailInStorage.getProgramNumber());
                            storage.setProduceFactoryName(detailInStorage.getProduceFactoryName());
                            storage.setSequence(detailInStorage.getBatchNumber());
                            storage.setProductionDate(productionDate.get(material.getMaterialId()));
                            storage.setEndDate(endDate.get(material.getMaterialId()));
                        }
                        storage.setStoreageName(inStorage.getStoreageName());
                        storage.setEndDate(LocalDate.now());
                        storageRepository.save(storage);
                    }

                    modelMap.put("success", true);
                    modelMap.put("Msg", "入库成功");
                } else {
                    modelMap.put("success", false);
                    modelMap.put("Msg", "入库失败，请输入入库物料");
                }
            }

        }else {
                modelMap.put("Msg","入库失败，校验未通过，对应的物料和数量在map中！");
                modelMap.put("checkResult",checkResult);
        }
        return modelMap;
    }

    /**
     * 删除入库单
     * 1.传入参数，idList
     * 2.完成删除入库单的操作
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> deleteInStorage(List<Map<String,Integer>> idList) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        for (Map<String, Integer> map: idList) {
            if (inStorageRepository.existsById(map.get("id"))) {
                modelMap.put("success", true);
                modelMap.put("Msg", "删除入库单成功");
                modelMap.put("inStorage", inStorageRepository.getOne(map.get("id")));
                inStorageRepository.deleteById(map.get("id"));
            } else {
                modelMap.put("success", false);
                modelMap.put("Msg", "没有该入库单，删除入库单失败");
            }
        }

        return modelMap;
    }
    /**修改入库单
     * 1.传入参数，inStorageId以及界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成修改入库单的操作
     * @param inStorageModel
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> modifyInStorage(InStorageModel inStorageModel) {
        InStorage inStorage = modelToInStorage(inStorageModel);
        inStorage.setInStorageId(inStorageModel.getInStorageId());
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (inStorageRepository.existsById(inStorage.getInStorageId())) {
            modelMap.put("success", true);
            modelMap.put("Msg", "修改入库单成功");
            modelMap.put("Storage", inStorageRepository.getOne(inStorage.getInStorageId()));
            inStorageRepository.save(inStorage);
        } else {
            modelMap.put("success", false);
            modelMap.put("Msg", "没有该入库单，修改入库单失败");
        }
        return modelMap;
    }
    /**查询所有入库单
     * 1.传入参数为空
     * 2.完成查询所有入库单的操作
     * @return
     */
    @Override
    public Map<String, Object> findAllInStorage() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<InStorage> inStorages = inStorageRepository.findAll();
        List<InStorageModel> inStorageModel = new ArrayList<>();
        for (InStorage inStorage : inStorages) {
            InStorageModel inStorageModel1 = inStorageToModel(inStorage);
            inStorageModel.add(inStorageModel1);
        }
        if (!inStorages.isEmpty()) {
            modelMap.put("success", true);
            modelMap.put("Msg", "获取入库列表成功");
            modelMap.put("inStorageModel", inStorageModel);
        } else {
            modelMap.put("success", false);
            modelMap.put("Msg", "入库记录为空，获取入库记录失败");
        }
        return modelMap;
    }
    /**通过id查询入库单
     * 1.传入参数id
     * 2.完成通过id的值查询入库单的操作
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findInStorageById(Integer id) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (inStorageRepository.existsById(id)) {
            InStorage inStorage = inStorageRepository.getOne(id);
            InStorageModel inStorageModel = inStorageToModel(inStorage);
            modelMap.put("success", true);
            modelMap.put("Msg", "通过id获取入库记录成功");
            modelMap.put("Storage", inStorageModel);
        } else {
            modelMap.put("success", false);
            modelMap.put("Msg", "没有该入库记录，获取入库记录失败");
        }
        return modelMap;
    }

    /**
     * @param exeState
     * @return
     */
    @Override
    public Map<String, Object> findInStorageByExeState(Integer exeState) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
   //     List<InStorageModel> inStorageModels = new ArrayList<>();
//        List<InStorage> inStorages = inStorageRepository.findByExeState(exeState);
//        if (!inStorages.isEmpty()){
//            for (InStorage inStorage : inStorages) {
//                InStorageModel inStorageModel = inStorageToModel(inStorage);
//                inStorageModels.add(inStorageModel);
//            }
//             modelMap.put("success", true);
//             modelMap.put("Msg", "通过状态获取入库记录成功");
//             modelMap.put("inStorageListModels", inStorageModels);
//              }
//        else {
//            modelMap.put("success",false);
//            modelMap.put("Msg","没有这种类型的入库记录，获取入库记录失败");
//        }
        return modelMap;
    }

    /**inStorage转换为inStorageModel
     * @param inStorage
     * @return
     */
    @Override
    public InStorageModel inStorageToModel(InStorage inStorage) {
            InStorageModel inStorageModel=new InStorageModel();

            float totalPrice=0;
            List<DetailStorageModel> detailStorageModels=new ArrayList<>();
            List<DetailInStorage> detailInStorages=inStorage.getDetailInStorages();
            if(!detailInStorages.isEmpty()) {
                DetailStorageModel detailStorageModel=new DetailStorageModel();
                for (DetailInStorage detailInStorage : detailInStorages) {
                    totalPrice = totalPrice + detailInStorage.getNumber() * detailInStorage.getPrice();
                    detailStorageModel.setNumber(detailInStorage.getNumber());//入库数量
                    detailStorageModel.setPrice(detailInStorage.getPrice());//单价
                    detailStorageModel.setTotal(detailInStorage.getPrice()*detailInStorage.getNumber());//amount*price（出入库）
                    detailStorageModel.setSequence(detailInStorage.getBatchNumber());//批次号（出入库）
                    detailStorageModel.setProgramNumber(detailInStorage.getProgramNumber());//项目号（出入库）
                    detailStorageModel.setDetailPos(detailInStorage.getDetailPos());//存放位置（入库）
                    detailStorageModel.setProduceFactoryName(detailInStorage.getProduceFactoryName());//生产厂家（入库）
                    detailStorageModels.add(detailStorageModel);
                    Material material=detailInStorage.getMaterial();
                    if(!StringUtils.isEmpty(material)){
                        detailStorageModel.setName(material.getName());
                        detailStorageModel.setModelNumber(material.getModelNumber());
                        detailStorageModel.setCode(material.getCode());
                    }
                }

            }

            inStorageModel.setInStoreDate(inStorage.getInStoreDate());//入库日期
            inStorageModel.setDeliverer(inStorage.getDeliverer());//入库人员
            inStorageModel.setRemark(inStorage.getRemark()); //备注
            inStorageModel.setRequestNo(inStorage.getRequestNo());//申请单号
            inStorageModel.setInStorageId(inStorage.getInStorageId());//入库id
            inStorageModel.setType(inStorage.getType());//入库类型
            inStorageModel.setStoreageName(inStorage.getStoreageName());//仓库名称
            inStorageModel.setDeliveryman(inStorage.getDeliveryman());//交货人
            inStorageModel.setOrderNumber(inStorage.getOrderNumber());//订单号
            inStorageModel.setList(detailStorageModels);
            return  inStorageModel;
    }

    /**inStorageModel转换为inStorage
     * @param inStorageModel
     * @return
     */
    @Override
    public InStorage modelToInStorage(InStorageModel inStorageModel) {
        InStorage inStorage = new InStorage();
        List<DetailInStorage> detailInStorages = new ArrayList<>();
        inStorage.setProgramNumber(inStorageModel.getProgramNumber());
        inStorage.setRequestNo(inStorageModel.getRequestNo());//申请单号
        inStorage.setOrderNumber(inStorageModel.getOrderNumber());//订单号
        inStorage.setRemark(inStorageModel.getRemark());//备注
        inStorage.setType(inStorageModel.getType()); //入库类型
        inStorage.setStoreageName(inStorageModel.getStoreageName());//仓库名称
        List<DetailStorageModel> detailStorageModels =inStorageModel.getList();
        if(!StringUtils.isEmpty(detailStorageModels)) {
            for (DetailStorageModel detailStorageModel : detailStorageModels) {
                DetailInStorage detailInStorage = new DetailInStorage();
                detailInStorage.setPrice(detailStorageModel.getPrice());//单价
                detailInStorage.setNumber(detailStorageModel.getNumber());//入库数量
                detailInStorage.setBatchNumber(detailStorageModel.getSequence());//批次号
                detailInStorage.setProgramNumber(detailStorageModel.getProgramNumber());//项目号
                detailInStorage.setDetailPos(detailStorageModel.getDetailPos());//存放位置
                detailInStorage.setSeller(detailStorageModel.getSellerName());
                detailInStorage.setProduceFactoryName(detailStorageModel.getProduceFactoryName());//生产厂家
                Material material=materialRepository.getOne(detailStorageModel.getMaterialId());
                detailInStorage.setMaterial(material);
                detailInStorages.add(detailInStorage);
            }
        }
        inStorage.setDetailInStorages(detailInStorages);

        return inStorage;
    }


    /**校验入库单和质检单物料数量
     * @param listInStorageModel
     * @return
     */
    @Override
    public Map<String,Integer> checkInStorage(ListInStorageModel listInStorageModel) {
       Map<String,Integer> map=new HashMap<>();
       List<Integer> qualityAuditIdList=listInStorageModel.getQualityAuditIdList();
       List<InStorageModel>inStorageModels=listInStorageModel.getInStorageModels();
        for (Integer qualityAuditId:qualityAuditIdList) {
            QualityAudit qualityAudit=qualityAuditRepository.getOne(qualityAuditId);
            List<DetailQualityAudit> detailQualityAudits=qualityAudit.getDetailQualityAudits();
            for (DetailQualityAudit detailQualityAudit:detailQualityAudits) {
                   Material material=detailQualityAudit.getDetailPurchase().getMaterial();
                   int inStorageCount=detailQualityAudit.getInStorageCount();
                for (InStorageModel inStorageModel:inStorageModels) {
                    InStorage inStorage=modelToInStorage(inStorageModel);
                    List<DetailInStorage>detailInStorages=inStorage.getDetailInStorages();
                    for (DetailInStorage detailInStorage:detailInStorages) {
                        if(material.getMaterialId()==detailInStorage.getMaterial().getMaterialId()){
                            inStorageCount=inStorageCount-detailInStorage.getNumber();
                            if(inStorageCount!=0) {
                                String name=material.getName();
                                map.put(name, inStorageCount);
                            }
                        }
                    }
                }
            }
        }

        return map;
    }
}
