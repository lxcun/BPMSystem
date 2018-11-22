package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.Purchase;
import com.atw.bpmsystem.Models.FindPurchaseModel;
import com.atw.bpmsystem.Models.PurchaseModel;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购接口，采购的增删改查，审批审核
 */
@Service
public interface PurchaseService {
    /**
     * 新建采购申请（由采购人员新建采购申请）
     * 需要传入申请采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面查看
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> addPurchase(PurchaseModel purchaseModel, Principal principal);

    /**
     * 提交采购申请（由采购人员提交采购申请）
     * 需要传入采购申请的purchaseId以及采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面
     * 格式和新建时相同
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> savePurchase(PurchaseModel purchaseModel, Principal principal);

    /**
     * 删除采购申请（操作人待定）
     * 传入参数为purchaseId的清单
     * @param idList
     * @return
     */
    public Map<String, Object> deletePurchase(List<Map<String,Integer>> idList);

    /**
     * 修改采购申请（申请人操作）
     * 1.当选择保存和需要修改时调用
     * 2.传入参数为purchaseId和其他purchase所有字段
     * @param purchaseModel
     * @return
     */
    public Map<String, Object> modifyPurchase(PurchaseModel purchaseModel);

    /**
     * 部门经理审批（财务调用）
     * 1.当进行审核时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> divisionManagerApprovalPurchase(PurchaseModel purchaseModel,Principal principal);

    /**
     * 高管审批（高管调用）
     * 1.当进行审批时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> seniorExecutiveApprovalPurchase(PurchaseModel purchaseModel,Principal principal);

    /**
     * 采购完成采购确认（采购调用）
     * 1.当进行采购下单后需调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> finishPurchase(PurchaseModel purchaseModel,Principal principal);


    /**等待质检（采购调用）
     * 1.当进行采购完成需要通知质检检验时调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    public Map<String, Object> waitQuality(PurchaseModel purchaseModel,Principal principal);
    /**
     * 查找所有采购
     * 1.传入参数为空
     * 2.查找结果为所有采购的列表
     * @return
     */
    public Map<String, Object> findAllPurchase();

    /**通过Id查找采购
     * @param purchaseModel
     * @return
     */
    public Map<String, Object> findPurchaseById(PurchaseModel purchaseModel);


    /**通过条件查询采购
     * 1.传入参数：条件列表参考FindPurchaseModel
     * 2.完成通过给定的条件查询采购
     * @param findPurchaseModels
     * @return
     */
    public Map<String, Object> findCondition(List<FindPurchaseModel> findPurchaseModels);
    /**
     * 提交查找（由采购人员调用当前接口）
     * 1.实现查找我保存的但是未提交的采购申请
     * 2.实现查找我历史提交的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findMyApplyPurchase(Principal principal);

    /**
     * 审核查找（由财务经理调用当前接口）
     * 1.实现查找我能审核的采购申请
     * 2.实现查找我历史审核的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findMyApprovalPurchase(Principal principal);

    /**
     * 审批查询（由高管调用当前接口）
     * 1.实现查找我能审批的采购申请
     * 2.实现查找我历史审批的采购申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findHighApprovalPurchase(Principal principal);
    /**
     * 待采购查询（由采购调用当前接口）
     * 1.实现查找我已审批的采购处于等待采购的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findWaitPurchase(Principal principal);
    /**
     * 采购完成查询（由采购调用当前接口）（已采购但未到货）
     * 1.实现查找我已完成的采购处于等待通知质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findFinishPurchase(Principal principal);
    /**待质检查询（由质检调用当前接口）
     * 1.实现查找我已采购完成的采购处于等待质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findWaitQuality(Principal principal);
    /**
     * 显示和实体转换
     * 实现将界面的采购转换为实体的采购
     * @param purchaseModel
     * @return
     */
    public Purchase modelToPurchase(PurchaseModel purchaseModel);

    /**
     *  显示和实体转换
     * 实现将实体的采购转换为界面的采购
     * @param purchase
     * @return
     */
    public PurchaseModel purchaseToModel(Purchase purchase);


}
