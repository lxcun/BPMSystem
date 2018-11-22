package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Entities.Purchase;
import com.atw.bpmsystem.Models.FindPurchaseModel;
import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Models.PurchaseModel;
import com.atw.bpmsystem.Services.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购接口，采购的增删改查，审批审核
 */
@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 36000)
@RestController
@Slf4j
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    /**
     * 新建采购申请（由采购人员新建采购申请）
     * 需要传入申请采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面查看
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/addPurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addPurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {
        return purchaseService.addPurchase(purchaseModel,principal);
    }
    /**
     * 提交采购申请（由采购人员提交采购申请）
     * 需要传入采购申请的purchaseId以及采购时界面上的所有字段（在PurchaseModel和DetailPurchaseModel）里面
     * 格式和新建时相同
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/savePurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> savePurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.savePurchase(purchaseModel,principal);
    }

    /**
     * 修改采购申请（申请人操作）
     * 1.当选择保存和需要修改时调用
     * 2.传入参数为purchaseId和其他purchase所有字段
     * @param purchaseModel
     * @return
     */
    @RequestMapping(value = "/api/modifyPurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> modifyPurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.modifyPurchase(purchaseModel);
    }
    /**
     * 部门经理审批（财务调用）
     * 1.当进行审核时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/divisionManagerApprovalPurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> divisionManagerApprovalPurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.divisionManagerApprovalPurchase(purchaseModel,principal);
    }
    /**
     * 高管审批（高管调用）
     * 1.当进行审批时调用这个接口
     * 2.传入参数为purchaseId以及处理结果和意见
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/seniorExecutiveApprovalPurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> seniorExecutiveApprovalPurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.seniorExecutiveApprovalPurchase(purchaseModel,principal);
    }
    /**
     * 采购完成采购确认（采购调用）
     * 1.当进行采购下单后需调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/finishPurchase", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> finishPurchase(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.finishPurchase(purchaseModel,principal);
    }
    /**等待质检（采购调用）
     * 1.当进行采购完成需要通知质检检验时调用这个接口
     * 2.传入参数为purchaseId
     * @param purchaseModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/waitQuality", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> waitQuality(@RequestBody PurchaseModel purchaseModel, Principal principal)
    {

        return purchaseService.waitQuality(purchaseModel,principal);
    }

    /**
     * 查找所有采购
     * 1.传入参数为空
     * 2.查找结果为所有采购的列表
     * @return
     */
    @RequestMapping(value = "/api/findAllPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllPurchase()
    {
        return purchaseService.findAllPurchase();
    }
    /**查找符合条件的所有采购
     * 1.传入参数为转换结构之后的FindPurchaseModel列表，字段参照FindPurchaseModel
     * 2.查找结果为符合条件的所有采购的列表
     * @return
     */
    @RequestMapping(value = "/api/findPurchaseByCondition", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findPurchaseByCondition(@RequestBody List<FindPurchaseModel> findPurchaseModelS)
    {
        log.info("进入采购条件查询接口");
        return purchaseService.findCondition(findPurchaseModelS);
    }
    /**
     * 提交查找（由采购人员调用当前接口）
     * 1.实现查找我保存的但是未提交的采购申请
     * 2.实现查找我历史提交的采购申请
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyApplyPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyApplyPurchase(Principal principal)
    {
        return purchaseService.findMyApplyPurchase(principal);
    }
    /**
     * 审核查找（由财务经理调用当前接口）
     * 1.实现查找我能审核的采购申请
     * 2.实现查找我历史审核的采购申请
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyApprovalPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyApprovalPurchase(Principal principal)
    {
        return purchaseService.findMyApprovalPurchase(principal);
    }
    /**
     * 审批查询（有高管调用当前接口）
     * 1.实现查找我能审批的采购申请
     * 2.实现查找我历史审批的采购申请
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findHighApprovalPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findHighApprovalPurchase(Principal principal)
    {
        return purchaseService.findHighApprovalPurchase(principal);
    }
    /**
     * 待采购查询（由采购调用当前接口）
     * 1.实现查找我已审批的采购处于等待采购的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findWaitPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findWaitPurchase(Principal principal)
    {
        return purchaseService.findWaitPurchase(principal);
    }
    /**
     * 采购完成查询（由采购调用当前接口）（已采购但未到货）
     * 1.实现查找我已完成的采购处于等待通知质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findFinishPurchase", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findFinishPurchase(Principal principal)
    {
        return purchaseService.findFinishPurchase(principal);
    }
    /**待质检查询（由质检调用当前接口）
     * 1.实现查找我已采购完成的采购处于等待质检的申请
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findWaitQuality", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findWaitQuality(Principal principal)
    {
        return purchaseService.findWaitQuality(principal);
    }

}

