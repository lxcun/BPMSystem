package com.atw.bpmsystem.Controllers;

        import com.atw.bpmsystem.Models.FindOutStorageModel;
        import com.atw.bpmsystem.Models.OutStorageModel;
        import com.atw.bpmsystem.Repositories.StorageRepository;
        import com.atw.bpmsystem.Services.OutStorageService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
        import org.springframework.http.MediaType;
        import org.springframework.web.bind.annotation.*;

        import java.security.Principal;
        import java.time.LocalDate;
        import java.util.Date;
        import java.util.List;
        import java.util.Map;

@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",maxAge = 3600)
@RestController
public class OutStorageController {
    @Autowired
    private OutStorageService outStorageService;
    @Autowired
    private StorageRepository storageRepository;
    /**新建出库（开发人员调用）
     * 1.需要传入的参数出库界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.实现出库单的保存操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/addOutStorage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addOutStorage(@RequestBody  OutStorageModel outStorageModel,Principal principal)
    {

        return outStorageService.addOutStorage(outStorageModel,principal);
    }
    /**
     * 出库单的提交（开发人员调用）
     * 1.需要传入的参数outStorageId
     * 2.实现单的提交操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/saveOutStorage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> saveOutStorage(@RequestBody  OutStorageModel outStorageModel,Principal principal)
    {
        return outStorageService.saveOutStorage(outStorageModel,principal);
    }
    /**
     * 删除出库单
     * 1.需要传入的参数id的表单
     * 2.实现出库单的删除操作
     * @param idList
     * @return
     */
    @RequestMapping(value = "/api/deleteOutStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> deleteOutStorage(@RequestBody List<Map<String,Integer>> idList)
    {
        return outStorageService.deleteOutStorage(idList);
    }
    /**
     * 出库单修改
     * 1.需要传入的参数utStorageId以及界面显示的各个字段（参考OutStorageModel和DetailStorageModel）
     * 2.完成出库单修改操作
     * @param outStorageModel
     * @return
     */
    @RequestMapping(value = "/api/modefyOutStorage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> modifyOutStorage(@RequestBody  OutStorageModel outStorageModel)
    {
        return outStorageService.modifyOutStorage(outStorageModel);
    }
    /**
     * 审核出库（部门经理操作）
     * 1.传入参数outStorageId，approver,auditComments,exeState,histories
     * 2.完成出库单审核操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/divisionManagerApproval", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> divisionManagerApproval(@RequestBody OutStorageModel outStorageModel,Principal principal)
    {
        return outStorageService.divisionManagerApproval(outStorageModel,principal);
    }
    /**
     * 审批出库（高管操作）
     * 1.传入参数outStorageId，approver,approvalComments,exeState,histories
     * 2.完成出库单审批操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/seniorExecutiveApproval", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> seniorExecutiveApproval(@RequestBody OutStorageModel outStorageModel,Principal principal)
    {

        return outStorageService.seniorExecutiveApproval(outStorageModel,principal);
    }
    /**
     * 出库操作（库管人员）
     * 1.传入参数outStorageModel，exeState,histories
     * 2.完成出库操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/approvalOutStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> approvalOutStorage(@RequestBody OutStorageModel outStorageModel,Principal principal)
    {

        return outStorageService.approvalOutStorage(outStorageModel,principal);
    }
    /**保存出库单（库管人员）
     * 1.传入参数outStorageModel
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/saveOutStorageKeeper", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> saveOutStorageKeeper(@RequestBody OutStorageModel outStorageModel,Principal principal)
    {

        return outStorageService.saveOutStorageKeeper(outStorageModel,principal);
    }

    /**确认出库（开发人员）
     * 1.传入参数outStorageId
     * 2.完成出库确认操作
     * @param outStorageModel
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/confirmOutStorage", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> confirmOutStorage(@RequestBody OutStorageModel outStorageModel,Principal principal)
    {

        return outStorageService.confirmOutStorage(outStorageModel,principal);
    }

    /**
     * 查询所有出库单
     * 1.传入参数为空
     * 2.完成所有出库单的查询
     * @return
     */
    @RequestMapping(value = "/api/findAllOutStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllOutStorage()
    {
        return outStorageService.findAllOutStorage();
    }
    /**通过传入的参数条件查找出库申请单（部门经理或者高管调用）
     * 1.传入参数type,department,programNumber,requestor,startTime,endTime(参照FindOutStorageModel)
     * @return
     */
    @RequestMapping(value = "/api/findAllOutStorageByCondition", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> findAllOutStorageByCondition(@RequestBody FindOutStorageModel findOutStorageModel)
    {
        Integer type=findOutStorageModel.getType();
        String department=findOutStorageModel.getDepartment();
        String programNumber=findOutStorageModel.getProgramNumber();
        String requestor=findOutStorageModel.getRequestor();
        LocalDate startTime=findOutStorageModel.getStartTime();
        LocalDate endTime=findOutStorageModel.getEndTime();
        return outStorageService.findAllOutStorageByCondition(type,department,programNumber,requestor,startTime,endTime);
    }

    /**通过id查询出库单
     * 1.传入参数outStorageId
     * 2.通过传入的id查询出库单
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/findOutStorageById", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findOutStorageById(Integer id)
    {
        return outStorageService.findOutStorageById(id);
    }
    /**
     * 待修改和提交查询（由开发人员调用当前接口）
     * 1.实现查询我已经保存但是没有提交的出库申请
     * 2.实现查询我已经提交的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyApplyOutStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyApplyOutStorage(Principal principal)
    {
        return outStorageService.findMyApplyOutStorage(principal);
    }
    /**
     * 待审核查询（由部门经理调用当前接口）
     * 1.实现查找我能审核的出库申请
     * 2.实现查找我历史审核的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyApprovalOutStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyApprovalOutStorage(Principal principal)
    {
        return outStorageService.findMyApprovalOutStorage(principal);
    }
    /**
     * 待审批查询（由高管调用当前接口）
     * 1.实现查找我能审批的出库申请
     * 2.实现查找我历史审批的出库申请
     * 3.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findHighApprovalOutStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findHighApprovalOutStorage(Principal principal)
    {
        return outStorageService.findHighApprovalOutStorage(principal);
    }
    /**待出库查询
     * 1.查询所有的待出库申请记录
     * 2.传入参数为空
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyOutStorage", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyOutStorage(Principal principal)
    {
        return outStorageService.findMyOutStorage(principal);
    }
    /**
     * 采购人员查询等待的出库单（等待状态查询）
     * 1.传入参数为空
     * 2.查询采购人员需要采购的出库单
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyOutStorageToPurchase",  method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyOutStorageToPurchase(Principal principal)
    {

        return outStorageService.findMyOutStorageToPurchase(principal);
    }
    /**
     * 申请人员查询出库完成的出库单
     * 1.传入参数为空
     * 2.完成查询出库完成的出库单的操作
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/findMyOutStorageToConfirm", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyOutStorageToConfirm(Principal principal)
    {

        return outStorageService.findMyOutStorageToConfirm(principal);
    }
    /**
     * 库管人员已经保存的出库单查询
     * 1.传入参数为outStoreKeeperId
     * @param outStoreKeeperId
     * @return
     */
    @RequestMapping(value = "/api/findOutStorageKeeperByOutStorageId",  method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findOutStorageKeeperByOutStorageId(Integer outStoreKeeperId)
    {

        return outStorageService.findOutStorageKeeperByOutStorageId(outStoreKeeperId);
    }
    /**
     * 库管人员的出库单查询
     * 1.传入参数为空
     * @return
     */
    @RequestMapping(value = "/api/findAllOutStorageKeeper",  method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findAllOutStorageKeeper()
    {
        return outStorageService.findAllOutStorageKeeper();
    }


}
