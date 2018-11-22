package com.atw.bpmsystem.Controllers;

import com.atw.bpmsystem.Models.UserModel;
import com.atw.bpmsystem.Repositories.UserRepository;
import com.atw.bpmsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@EnableAutoConfiguration
@CrossOrigin(allowCredentials="true",origins = "*",maxAge = 36000)
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**注册（添加）新用户
     * 1.传入参数为用户的所有字段（参考userModel）
     * 2.完成用户的新建操作
     * @param userModel
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/api/registerUser", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody UserModel userModel) {
       return userService.registerUser(userModel);
    }

    /**验证用户登录名和密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户信息确认操作
     * @param userModel
     * @return
     */
    @RequestMapping(value = "/api/verifyUser", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> verifyUser(@RequestBody Map<String,String> userModel) {

        return userService.verifyUser(userModel.get("userName"),userModel.get("password"));
    }

    /**修改用户信息（系统管理员操作）
     * 1.传入参数id以及界面的所有字段（参考UserModel）
     * 2.完成用户信息的修改操作（传入密码修改无效）
     * @param userModel
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/api/modifyUser", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> modifyUser(@RequestBody UserModel userModel) {
        return userService.modifyUser(userModel);
    }

    /**删除用户（系统管理员操作）
     * 1.传入参数idList
     * 2.完成删除用户的操作
     * @param idList
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/deleteUser")
    @ResponseBody
    public Map<String, Object> deleteUser( @RequestBody List<Map<String,Integer>> idList) {
        return userService.deleteUser(idList);
    }

    /**修改密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户密码的修改
     * @param userModel
     * @return
     */
    @RequestMapping(value = "/api/modifyPassword", method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public Map<String, Object> modifyPassword(@RequestBody Map<String,String> userModel) {
        return userService.modifyPassword(userModel.get("userName"),userModel.get("password"));
    }

    /**初始化密码操作（系统管理员调用）
     * 1.传入参数id
     * 2.完成所选id的用户的密码初试化操作
     * 3.初始化密码为123456
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/originalPassword")
    @ResponseBody
    public Map<String, Object> originalPassword( @RequestBody Map<String,Integer> id) {
        return userService.originalPassword(id.get("id"));
    }

    /**查询用户列表
     * 1.传入参数为空
     * 2.完成查询所有的用户的操作
     * @return
     */
    @RequestMapping(value = "/api/listUser", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> listUser() {
        return userService.listUser();
    }
    /**查询高管列表
     * 1.传入参数为空
     * 2.完成查询所有的高管的操作
     * @return
     */
    @RequestMapping(value = "/api/listApprover", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> listApprover() {
        return userService.listApprover();
    }
    /**当前登录用户获取
     * 1.传入参数为空
     * 2.完成获取当前的用户的操作
     * @param principal
     * @return
     */
    @RequestMapping(value = "/api/username", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> currentUserName(Principal principal) {
        Map<String,Object> map=new HashMap<>();
        //User user=userRepository.findByLoginName(principal.getName());
        //UserModel userModel=userService.UserToUserModel(user);
        //List<Role>roles=userModel.getRoles();
        map.put("user",userRepository.findByLoginName(principal.getName()));
        return map;
    }
}