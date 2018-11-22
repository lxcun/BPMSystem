package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
   /**通过角色名查找角色
    * @param role
    * @return
    */
   Role findByRole(String role);
}
