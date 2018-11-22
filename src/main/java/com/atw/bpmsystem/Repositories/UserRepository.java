package com.atw.bpmsystem.Repositories;

import com.atw.bpmsystem.Entities.Role;
import com.atw.bpmsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

   // public List<User> findByName(String name);

    /**通过登录名loginName查找用户
     * @param loginName
     * @return
     */
    public User findByLoginName(String loginName);

    /**通过部门operation和职位position查找用户
     * @param operation
     * @param position
     * @return
     */
    public User findByOperationAndPosition(String operation,String position);

    /**通过登录名loginName和密码password查找用户
     * @param loginName
     * @param password
     * @return
     */
    public User findByLoginNameAndPassword(String loginName, String password);

    /**通过部门operation和角色列表roles查找用户
     * @param operation
     * @param roles
     * @return
     */
    public List<User> findByOperationAndRoles(String operation,List<Role> roles);

    /**通过角色列表roles查找用户
     * @param roles
     * @return
     */
    public List<User> findByRoles(List<Role> roles);

    /**通过部门operation查找用户
     * @param operation
     * @return
     */
    public List<User>findByOperation(String operation);
//    //从数据库查询东西
//    @Query("from User u where u.=:name")
//    //@param是参数的解释。
//    public List<User> findUser(@Param("loginName") String loginName);

    //  public Integer personnelIdentification(String user_identity);
}

