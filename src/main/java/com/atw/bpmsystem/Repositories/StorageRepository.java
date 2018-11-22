package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Storage;
import com.atw.bpmsystem.Entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage,Integer> {
        /**通过物料material查找库存
         * @param material
         * @return
         */
        List<Storage> findByMaterial(Material material);

        /**通过物料material和批次号sequence查找库存
         * @param material
         * @param sequence
         * @return
         */
        Storage findByMaterialAndSequence(Material material,String sequence);

        /**通过条件查询库存表
         * @param type
         * @param code
         * @param modelNumber
         * @param encapsulation
         * @param programNumber
         * @return
         */
        @Query(value = "select distinct storage_id from find_storage where if(?1 !='',type=?1,1=1) and if(?2 !='',code=?2,1=1)" +
                "and if(?3 !='',model_number=?3,1=1)  "+ "and if(?4 !='',encapsulation=?4,1=1) and if(?5 !='',program_number=?5,1=1)  ",nativeQuery = true)
        List<Integer> findByCondition(Type type, String code, String modelNumber, String encapsulation,String programNumber);

        /**查询库存表中的项目号
         * @return
         */
        @Query(value = "select distinct program_number from storage",nativeQuery = true)
        List<String> findProgramNumber();
}
