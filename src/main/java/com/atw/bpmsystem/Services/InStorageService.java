package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.InStorage;
import com.atw.bpmsystem.Models.InStorageModel;
import com.atw.bpmsystem.Models.ListInStorageModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public interface InStorageService {
    /**
     * 添加入库单
     * 1.传入参数，界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成添加入库单的操作
     * @param inStorageModel
     * @param principal
     * @return
     */
    public Map<String, Object> addInStorage(InStorageModel inStorageModel,Principal principal);

    /**
     * 批量添加入库单
     * 1.传入参数，界面上所有的入库字段（参照InStorageModel和DetailStorageModel）Lis格式
     * 2.完成批量添加入库单的操作
     * @return
     */
    public Map<String, Object> addInStorageList(ListInStorageModel listInStorageModel,Principal principal);
    /**
     * 删除入库单
     * 1.传入参数，idList
     * 2.完成删除入库单的操作
     * @param idList
     * @return
     */
    public Map<String, Object> deleteInStorage( List<Map<String,Integer>> idList);

//    /**
//     * @param inStorageModel
//     * @param principal
//     * @return
//     */
//    public Map<String, Object> approvalInStorage(InStorageModel inStorageModel,Principal principal);

    /**修改入库单
     * 1.传入参数，inStorageId以及界面上所有的入库字段（参照InStorageModel和DetailStorageModel）
     * 2.完成修改入库单的操作
     * @param inStorageModel
     * @return
     */
    public Map<String, Object> modifyInStorage(InStorageModel inStorageModel);
    /**查询所有入库单
     * 1.传入参数为空
     * 2.完成查询所有入库单的操作
     * @return
     */
    public Map<String, Object> findAllInStorage();
    /**通过id查询入库单
     * 1.传入参数id
     * 2.完成通过id的值查询入库单的操作
     * @param id
     * @return
     */
    public Map<String, Object> findInStorageById(Integer id);

    /**
     * @param exeState
     * @return
     */
    public Map<String, Object> findInStorageByExeState(Integer exeState);

    /**inStorage转换为inStorageModel
     * @param inStorage
     * @return
     */
    public InStorageModel inStorageToModel(InStorage inStorage);

    /**inStorageModel转换为inStorage
     * @param inStorageModel
     * @return
     */
    public InStorage modelToInStorage(InStorageModel inStorageModel);

    /**校验入库单和质检单物料数量
     * @param listInStorageModel
     * @return
     */
    public Map<String,Integer> checkInStorage(ListInStorageModel listInStorageModel);
}
