package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Material;
import com.atw.bpmsystem.Entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material,Integer>{
    /**通过preCode和code查找物料Material
     * @param preCode
     * @param code
     * @return
     */
    public Material findByPreCodeAndCode(String preCode,String code);

    /**
     * @param name
     * @param modelNumber
     * @param Encapsulation
     * @return
     */
    public Material findByNameAndModelNumberAndEncapsulation(String name,String modelNumber,String Encapsulation);

    /**
     * @param type
     * @param code
     * @param modelNumber
     * @param encapsulation
     * @return
     */
    @Query(value = "select * from material where if(?1 !='',type=?1,1=1) and if(?2 !='',code=?2,1=1)" +
            "and if(?3 !='',model_number=?3,1=1)  "+ "and if(?4 !='',encapsulation=?4,1=1)  ",nativeQuery = true)
    List<Material> find(Type type, String code,String modelNumber, String encapsulation);

    /**查询物料表中的的所有物料编码
     * @return
     */
    @Query(value = "select distinct code from material",nativeQuery = true)
    List<String> findMaterialCode();

    /**查询物料表中的所有物料型号
     * @return
     */
    @Query(value = "select distinct model_number from material",nativeQuery = true)
    List<String> findMaterialModelNumber();

    /**查询物料表中的所有物料封装
     * @return
     */
    @Query(value = "select distinct encapsulation from material",nativeQuery = true)
    List<String> findMaterialEncapsulation();

}
