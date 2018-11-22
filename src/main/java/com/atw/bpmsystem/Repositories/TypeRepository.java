package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type,Integer>{
    /**通过类型名称typeName查找类型
     * @param typeName
     * @return
     */
    public Type findByTypeName(String typeName);
}
