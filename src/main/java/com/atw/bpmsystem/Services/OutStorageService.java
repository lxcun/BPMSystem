package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.OutStorage;
import com.atw.bpmsystem.Entities.OutStoreKeeper;
import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Models.PurchaseModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface OutStorageService {
    /**新建出库（开发人员调用）
     * 1.需要传入的参数出库界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.实现出库单的保存操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> addOutStorage(OutStorageModel outStorageModel,Principal principal);
    /**
     * 出库单的提交（开发人员调用）
     * 1.需要传入的参数outStorageId
     * 2.实现单的提交操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> saveOutStorage(OutStorageModel outStorageModel,Principal principal);
    /**
     * 删除出库单
     * 1.需要传入的参数id的表单
     * 2.实现出库单的删除操作
     * @param idList
     * @return
     */
    public Map<String, Object> deleteOutStorage(List<Map<String,Integer>> idList);
    /**
     * 出库单修改
     * 1.需要传入的参数utStorageId以及界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.完成出库单修改操作
     * @param outStorageModel
     * @return
     */
    public Map<String, Object> modifyOutStorage(OutStorageModel outStorageModel);
    /**
     * 审核出库（部门经理操作）
     * 1.传入参数outStorageId，approver,auditComments,exeState,histories
     * 2.完成出库单审核操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> divisionManagerApproval(OutStorageModel outStorageModel,Principal principal);
    /**
     * 审批出库（高管操作）
     * 1.传入参数outStorageId，approver,approvalComments,exeState,histories
     * 2.完成出库单审批操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object>seniorExecutiveApproval(OutStorageModel outStorageModel,Principal principal);

    /**保存出库单（库管人员）
     * 1.传入参数outStorageModel
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> saveOutStorageKeeper(OutStorageModel outStorageModel,Principal principal);

    /**
     * 出库操作（库管人员）
     * 1.传入参数outStorageModel，exeState,histories
     * 2.完成出库操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> approvalOutStorage(OutStorageModel outStorageModel,Principal principal);
    /**
     * 确认出库（开发人员）
     * 1.传入参数outStorageId
     * 2.完成出库确认操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> confirmOutStorage(OutStorageModel outStorageModel,Principal principal);
    /**
     * 查出所有的出库清单
     * 1.查出所有的出库申请
     * 2.传入参数为空
     * @return
     */
    public Map<String, Object> findAllOutStorage();

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
    public Map<String,Object> findAllOutStorageByCondition(Integer type, String department, String programNumber, String requestor, LocalDate startTime, LocalDate endTime);
    /**通过id查询出库单
     * 1.传入参数outStorageId
     * 2.通过传入的id查询出库单
     * @param id
     * @return
     */
    public Map<String, Object> findOutStorageById(Integer id);

    /**
     * 待修改和提交查询（由开发人员调用当前接口）
     * 1.实现查询我已经保存但是没有提交的出库申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findMyApplyOutStorage(Principal principal);
    /**
     * 待审核查询（由部门经理调用当前接口）
     * 1.实现查找我能审核的出库申请
     * 2.实现查找我历史审核的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findMyApprovalOutStorage(Principal principal);

    /**
     * 待审批查询（由高管调用当前接口）
     * 1.实现查找我能审批的出库申请
     * 2.实现查找我历史审批的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findHighApprovalOutStorage(Principal principal);

    /**待出库查询
     * 1.查询所有的待出库申请记录
     * 2.传入参数为空
     * @param principal
     * @return
     */
    public Map<String, Object> findMyOutStorage(Principal principal);

    /**
     * 采购人员查询等待的出库单（等待状态查询）
     * 1.传入参数为空
     * 2.查询采购人员需要采购的出库单
     * @param principal
     * @return
     */
    public Map<String, Object> findMyOutStorageToPurchase(Principal principal);
    /**
     * 申请人员查询出库完成的出库单
     * 1.传入参数为空
     * 2.完成查询出库完成的出库单的操作
     * @param
     * @return
     */
    public Map<String, Object> findMyOutStorageToConfirm(Principal principal);

    /**通过传入的outStorageId查询OutStoreKeeper
     * 1.传入参数outStorageId
     * @param outStorageId
     * @return
     */
    public Map<String, Object> findOutStorageKeeperByOutStorageId(Integer outStorageId);

    /**查询所有的出库单（库管填写的出库单）
     * @return
     */
    public Map<String, Object> findAllOutStorageKeeper();
    /**
     * 将model转化为出库申请清单
     * @param outStorageModel
     * @return
     */
    public OutStorage modelToOutStorage(OutStorageModel outStorageModel);
    /**
     * 将出库申请清单转换为model
     * @param outStorage
     * @return
     */
    public OutStorageModel outStorageToModel(OutStorage outStorage);

    /**将model转化为出库清单
     * @param outStorageModel
     * @return
     */
    public OutStoreKeeper modelToOutStoreKeeper(OutStorageModel outStorageModel);

}
