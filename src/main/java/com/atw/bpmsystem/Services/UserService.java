package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.User;
import com.atw.bpmsystem.Models.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    /**验证用户登录名和密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户信息确认操作
     */
    Map<String, Object> verifyUser(String userName,String password);
    /**注册（添加）新用户
     * 1.传入参数为用户的所有字段（参考userModel）
     * 2.完成用户的新建操作
     * @param userModel
     * @return
     */
    Map<String, Object> registerUser(UserModel userModel);

    /**删除用户（系统管理员操作）
     * 1.传入参数idList
     * 2.完成删除用户的操作
     * @param idList
     * @return
     */
    Map<String, Object> deleteUser(List<Map<String,Integer>> idList);
    /**修改用户信息（系统管理员操作）
     * 1.传入参数id以及界面的所有字段（参考UserModel）
     * 2.完成用户信息的修改操作（传入密码修改无效）
     * @param userModel
     * @return
     */
    Map<String, Object> modifyUser(UserModel userModel);

    /**修改密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户密码的修改
     *
     * @return
     */
    Map<String, Object> modifyPassword(String userName,String password);

    /**初始化密码操作（系统管理员调用）
     * 1.传入参数id
     * 2.完成所选id的用户的密码初试化操作
     * 3.初始化密码为123456
     * @param id
     * @return
     */
    Map<String, Object> originalPassword(Integer id);

    /**查询用户列表
     * 1.传入参数为空
     * 2.完成查询所有的用户的操作
     * @return
     */
    Map<String, Object> listUser();

    /**UserModel转换为User
     * @param userModel
     * @return
     */
    User UserModelToUser(UserModel userModel);

    /**User转换为UserModel
     * @param user
     * @return
     */
    UserModel UserToUserModel(User user);

    /**查询高管列表
     * 1.传入参数为空
     * 2.完成查询所有的高管的操作
     * @return
     */
    Map<String, Object> listApprover();
}
