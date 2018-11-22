package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.DetailOutStorage;
import com.atw.bpmsystem.Entities.OutStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OutStorageRepository extends JpaRepository<OutStorage,Integer> {

   /**通过申请号requestNo和状态exeState查找出库单
    * @param requestNo
    * @param exeStat
    * @return
    */
   public List<OutStorage> findOutStoragesByRequestNoAndExeStat(String requestNo,Integer exeStat);

   /**通过状态exeState查找出库单
    * @param exeState
    * @return
    */
   public List<OutStorage> findByExeStat(Integer exeState);

   /**通过申请人requestor查找出库单
    * @param requestor
    * @return
    */
   public List<OutStorage> findByRequestorOrderByExeStat(String requestor);

   /**通过审核人examiner和状态exeState查找出库单
    * @param examiner
    * @return
    */
   public List<OutStorage> findByExaminer(String examiner);

   /**通过审批人approver和状态exeState查找出库单
    * @param approver
    * @param exeState
    * @return
    */
   public List<OutStorage> findByApproverAndExeStat(String approver,Integer exeState);

   /**通过审批人approver和状态列表exeStates查找出库单
    * @param approver
    * @param exeStates
    * @return
    */
   public List<OutStorage> findByApproverAndExeStatIn(String approver,List<Integer> exeStates);

   /**通过申请人requestor和状态exeState查找出库单
    * @param requestor
    * @param exeState
    * @return
    */
   public List<OutStorage> findByRequestorAndExeStat(String requestor,Integer exeState);

   /**通过申请人requestor和状态列表exeStates查找出库单
    * @param requestor
    * @param exeStates
    * @return
    */
   public List<OutStorage> findByRequestorAndExeStatIn(String requestor,List<Integer> exeStates);

   /**通过部门department和状态列表exeStates查找出库单
    * @param department
    * @param exeState
    * @return
    */
   public List<OutStorage> findByDepartmentAndExeStat(String department,Integer exeState);

   /**通过状态列表exeStates查找出库单
    * @param exeStates
    * @return
    */
   public List<OutStorage> findByExeStatIn(List<Integer> exeStates);

   /**通过出库人auditor和状态exeState查找出库单
    * @param auditor
    * @param exeState
    * @return
    */
   public List<OutStorage> findByAuditorAndExeStat(String auditor,Integer exeState);

   /**通过出库人auditor查找出库单
    * @param auditor
    * @return
    */
   public List<OutStorage> findByAuditor(String auditor);

   /**通过给定条件查询
    * @param type
    * @param department
    * @param programNumber
    * @param requestor
    * @param startTime
    * @param endTime
    * @return
    */
   @Query(value = "select * from out_storage where if(?1 !='',type=?1,1=1) and if(?2 !='',department=?2,1=1)" +
           "and if(?3 !='',program_number=?3,1=1)  "+ "and if(?4 !='',requestor=?4,1=1)"+"and if(?5 !='',create_date>=?5,1=1)"+"and if(?6 !='',create_date<=?6,1=1) and exe_stat!=0",nativeQuery = true)
   List<OutStorage> find(Integer type, String department, String programNumber, String requestor, LocalDate startTime, LocalDate endTime);

}
