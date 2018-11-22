package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Purchase;
import com.atw.bpmsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
    /**通过状态exeState查找采购
     * @param exeState
     * @return
     */
    List<Purchase> findByExeState(Integer exeState);

    /**通过状态exeState和purchaseId查找采购
     * @param exeState
     * @param purchaseId
     * @return
     */
    List<Purchase>findByExeStateAndPurchaseId(Integer exeState,Integer purchaseId);

    /**通过审核人auditor查找采购
     * @param auditor
     * @return
     */
    List<Purchase>findByAuditor(User auditor);

    /**通过审批人approver和状态查找采购
     * @param approver
     * @return
     */
    List<Purchase>findByApproverAndExeState(User approver,Integer exeState);
    /**通过审批人approver和状态列表查找采购
     * @param approver
     * @return
     */
    List<Purchase>findByApproverAndExeStateIn(User approver,List<Integer> exeStates);

    /**通过申请人requestor和状态exeState查找采购
     * @param requestor
     * @param exeState
     * @return
     */
    List<Purchase>findByRequestorAndExeState(User requestor,Integer exeState);

    /**通过申请人requestor和状态列表exeStates查找采购
     * @param requestor
     * @param exeStates
     * @return
     */
    List<Purchase>findByRequestorAndExeStateIn(User requestor,List<Integer> exeStates);

//    @Query(value = "select * from purchase where if(?1 !='',requestor_id=?1,1=1) " +
//            "and if(?2 !='',auditor_id=?2,1=1)" +
//            "and if(?3 !='',approver_id=?3,1=1)  "+ "and if(?4 !='',exe_state=?4,1=1)"+
//            "and if(?5 !='',creat_date>=?5,1=1)"+"and if(?6 !='',creat_date<=?6,1=1)"+
//            "and if(?7 !='',audit_date>=?7,1=1)"+"and if(?8 !='',audit_date<=?8,1=1)"+
//            "and if(?9 !='',approval_time>=?9,1=1)"+"and if(?10 !='',approval_time<=?10,1=1)"+
//            "and if(?11 !='',order_date>=?11,1=1)"+"and if(?12 !='',order_date<=?12,1=1)"+
//            "and if(?13 !='',receiving_date>=?13,1=1)"+"and if(?14 !='',receiving_date<=?14,1=1)"+
//            "and if(?15 !='',in_storage_date>=?15,1=1)"+"and if(?16 !='',in_storage_date<=?16,1=1)"+
//            "and if(?17 !='',(select sum(detail_purchase.purchase_count) from detail_purchase where detail_purchase.purchase=purchase.purchase_id)>=?17,1=1)" +
//            "and if(?18 !='',(select sum(detail_purchase.purchase_count) from detail_purchase where detail_purchase.purchase=purchase.purchase_id)<=?18,1=1)" +
//            "and if(?19 !='',(select sum(detail_purchase.purchase_count*detail_purchase.purchase_price) from detail_purchase where detail_purchase.purchase=purchase.purchase_id)>=?19,1=1)" +
//            "and if(?20 !='',(select sum(detail_purchase.purchase_count*detail_purchase.purchase_price) from detail_purchase where detail_purchase.purchase=purchase.purchase_id)<=?20,1=1)" ,nativeQuery = true)
//    List<Purchase>findByCondition(User requestor, User auditor,
//                                  User approver, Integer exeState, Date createDate1,
//                                  Date createDate2,Date auditDate1,Date auditDate2,
//                                  Date approvalTime1,Date approvalTime2,
//                                  Date orderDate1,Date orderDate2,
//                                  Date receivingDate1,Date receievingDate2,
//                                  Date inStorageDate1,Date inStorageDate2,
//                                  Integer purchaseAccount1,Integer purchaseAccount2,
//                                  Integer totalPrice1,Integer totalPrice2
//    );



}
