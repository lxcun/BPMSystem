package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Entities.*;
import com.atw.bpmsystem.Models.InStorageModel;
import com.atw.bpmsystem.Models.OutStorageModel;
import com.atw.bpmsystem.Models.UserModel;
import com.atw.bpmsystem.Repositories.*;
import com.atw.bpmsystem.Services.InStorageService;
import com.atw.bpmsystem.Services.MailService;
import com.atw.bpmsystem.Services.OutStorageService;
import com.atw.bpmsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MailService mailService;
    /**验证用户登录名和密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户信息确认操作
     */
    @Override
    public Map<String, Object> verifyUser(String userName,String password) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //两者相同，返回true，否则返回false
        User user=userRepository.findByLoginNameAndPassword(userName, password);
        if (user.getLoginName()!=null||!(user.getLoginName().length() <= 0)) {
            modelMap.put("success",true);
            modelMap.put("Msg","用户"+user.getLoginName()+"登录成功");
            modelMap.put("User",user);
        } else {
            modelMap.put("success",false);
            modelMap.put("Msg","用户名或密码错误，登录失败");
        }
        return modelMap;
    }
    /**注册（添加）新用户
     * 1.传入参数为用户的所有字段（参考userModel）
     * 2.完成用户的新建操作
     * @param userModel
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> registerUser(UserModel userModel) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        User user=UserModelToUser(userModel);
        String title="用户注册！";
        String text="你注册了奥特为管理系统的账号！可以点击链接进入系统";
        if (userRepository.findByLoginName(user.getLoginName())==null||StringUtils.isEmpty(userRepository.findByLoginName(user.getLoginName())))
        {
            userRepository.save(user);
            modelMap.put("success",true);
            modelMap.put("Msg","用户"+user.getLoginName()+"注册成功");
            modelMap.put("User",user);
            if(user.getEmail()!=null)
            mailService.sendEmail(user.getEmail(),title,text);
        } else {
            modelMap.put("success",false);
            modelMap.put("Msg","用户名"+user.getLoginName()+"已经存在，注册失败");
        }
            return modelMap;
    }
    /**删除用户（系统管理员操作）
     * 1.传入参数idList
     * 2.完成删除用户的操作
     * @param idList
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> deleteUser(List<Map<String,Integer>> idList){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        for (Map<String,Integer> id:idList) {
            User user= userRepository.getOne(id.get("id"));
            if(userRepository.findByLoginName(user.getLoginName())!=null||!(StringUtils.isEmpty(userRepository.findByLoginName(user.getLoginName())))){
                userRepository.delete(user);
                modelMap.put("success",true);
                modelMap.put("Msg","删除"+user.getLoginName()+"成功");
            }
            else {
                modelMap.put("success",false);
                modelMap.put("Msg","用户名"+user.getLoginName()+"为空，删除失败");
            }

        }

        return modelMap;
    }
    /**修改用户信息（系统管理员操作）
     * 1.传入参数id以及界面的所有字段（参考UserModel）
     * 2.完成用户信息的修改操作（传入密码修改无效）
     * @param userModel
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> modifyUser(UserModel userModel){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        userModel.setPassword("123456");
        User user=UserModelToUser(userModel);
        User user1= userRepository.findByLoginName(user.getLoginName());
        if(user1.getLoginName()!=null||!(user1.getLoginName().length() <= 0)) {
            user1.setRoles(user.getRoles());
            user1.setPosition(user.getPosition());
            user1.setPhone(user.getPhone());
            user1.setOperation(user.getOperation());
            user1.setEmail(user.getEmail());
            user1.setName(user.getName());
            userRepository.save(user1);
            modelMap.put("success",true);
            modelMap.put("Msg",user1+"修改用户"+user+"信息成功");
            modelMap.put("User",user);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","当前用户信息不能存在，修改用户"+user.getLoginName()+"失败");
        }
        return modelMap;
    }
    /**修改密码
     * 1.传入参数为登录名userName和新密码password
     * 2.完成用户密码的修改
     *
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> modifyPassword(String userName,String password){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        User user=userRepository.findByLoginName(userName);
        if(user.getLoginName()!=null||!(user.getLoginName().length() <= 0)) {
            password=new BCryptPasswordEncoder().encode(password);
            user.setPassword(password);
            userRepository.save(user);
            modelMap.put("success",true);
            modelMap.put("Msg",user+"修改密码成功");
            modelMap.put("User",user);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","当前用户信息不能存在，修改用户"+user.getLoginName()+"密码失败");
        }
        return modelMap;
    }
    /**初始化密码操作（系统管理员调用）
     * 1.传入参数id
     * 2.完成所选id的用户的密码初试化操作
     * 3.初始化密码为123456
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> originalPassword(Integer id) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        User user=new User();
            if(id!=null){
                user=userRepository.getOne(id);
                if(user.getLoginName()!=null||!(user.getLoginName().length() <= 0)) {
                    String password="123456";
                    password=new BCryptPasswordEncoder().encode(password);
                    user.setPassword(password);
                     userRepository.save(user);
                     modelMap.put("success",true);
                     modelMap.put("Msg",user+"初始化密码成功");
                     modelMap.put("User",user);
                 }
                else {
                     modelMap.put("success",false);
                    modelMap.put("Msg","当前用户信息不能存在，初试化用户"+user.getLoginName()+"密码失败");
                }
            }
            else {
            modelMap.put("success",false);
            modelMap.put("Msg","请输入id，初始化密码失败");
            }
        return modelMap;

    }
    /**查询用户列表
     * 1.传入参数为空
     * 2.完成查询所有的用户的操作
     * @return
     */
    @Override
    public Map<String, Object> listUser(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<User> users= userRepository.findAll();
        if(!users.isEmpty()){
//            List<UserModel> userModels=new ArrayList<>();
//            for (User user:users) {
//               userModels.add(UserToUserModel(user));
//            }
            modelMap.put("success",true);
            modelMap.put("Msg","获取用户列表数据成功");
            modelMap.put("listUser",users);
        }
      else {
            modelMap.put("success",false);
            modelMap.put("Msg","用户数据为空，获取用户数据失败");
        }
        return modelMap;
    }

    /**初试化用户表存入默认用户
     * @throws Exception
     */
    @Transactional
    @PostConstruct
    public void init() throws Exception {
        System.out.println("JavaBean类init 方法");
        List<User> list = userRepository.findAll();
        if(list.size() <= 0){
            User user = new User();
            user.setLoginName("admin");
            String password = new BCryptPasswordEncoder().encode("123456");
            user.setPassword(password);
            List<Role> roles=new ArrayList<>();
                Role role=new Role();
                role.setRole("ROLE_ADMIN");
                roles.add(role);
            user.setRoles(roles);
            user.setName("系统管理员");
            userRepository.save(user);
        }
    }
    /**UserModel转换为User
     * @param userModel
     * @return
     */
    @Override
    public User UserModelToUser(UserModel userModel) {
        User user=new User();
        user.setId(userModel.getId());
        user.setLoginName(userModel.getLoginName());
        user.setName(userModel.getName());
        String password=userModel.getPassword();
        password=new BCryptPasswordEncoder().encode(password);
        user.setPassword(password);
        user.setEmail(userModel.getEmail());
        user.setOperation(userModel.getOperation());
        user.setPhone(userModel.getPhone());
        user.setPosition(userModel.getPosition());
        List<Role> roles=new ArrayList<>();
        Map<Integer,String> roleList=userModel.getRoles();
        if(!StringUtils.isEmpty(roleList)){
            for (int i=1;i<=7;i++)
            {    String role="";
                if(roleList.containsKey(i)){
                  role = roleList.get(i);
                Role role1=new Role();
                switch (role)
              {
                 case "ROLE_ADMIN":role1.setRole("ROLE_ADMIN");break;//系统管理员
                 case "ROLE_DEVELOPER": role1.setRole("ROLE_DEVELOPER");break;//开发人员
                 case "ROLE_DIVISIONMANAGER":role1.setRole("ROLE_DIVISIONMANAGER") ;break;//部门经理
                 case "ROLE_SENIOREXECUTIVE": role1.setRole("ROLE_SENIOREXECUTIVE");break;//高管
                 case "ROLE_BUYER": role1.setRole("ROLE_BUYER");break;//采购员
                 case "ROLE_INSPECTOR": role1.setRole("ROLE_INSPECTOR");break;//质检员
                 case "ROLE_STOREHOUSE": role1.setRole("ROLE_STOREHOUSE");break;//库管
                 case "ROLE_USER":role1.setRole("ROLE_USER");break;//普通用户
                 case "":role1.setRole("ROLE_USER");break;
                 // case "ROLE_USER":role1.setRole("ROLE_USER");
                 default:role1.setRole(role);break;
              }
              Role oldRole=roleRepository.findByRole(role1.getRole());
              if(StringUtils.isEmpty(oldRole))
              roles.add(role1);
              else roles.add(oldRole);
            }
            }

            user.setRoles(roles);
        }
        return user;
    }
    /**User转换为UserModel
     * @param user
     * @return
     */
    @Override
    public UserModel UserToUserModel(User user) {
        UserModel userModel=new UserModel();
//        userModel.setLoginName(user.getLoginName());
//        userModel.setPassword(user.getPassword());
//        userModel.setName(user.getName());
//        userModel.setEmail(user.getEmail());
//        userModel.setOperation(user.getOperation());
//        userModel.setPosition(user.getPosition());
//        userModel.setPhone(user.getPhone());
//        List<Role> roles=new ArrayList<>();
//        List<Role> roleList=user.getRoles();
//        if(!StringUtils.isEmpty(roleList)){
//            for (Role role:roleList) {
//                Role role1=new Role();
//                String roleName=role.getRole().replaceFirst("ROLE_","");
//                switch (roleName)
//                {
//                    case "ADMIN":role1.setRole("系统管理员");break;
//                    case "DEVELOPER": role1.setRole("开发人员");break;
//                    case "DIVISIONMANAGER":role1.setRole("部门经理") ;break;
//                    case "SENIOREXECUTIVE": role1.setRole("高管");break;
//                    case "BUYER": role1.setRole("采购员");break;
//                    case "INSPECTOR": role1.setRole("质检员");break;
//                    case "STOREHOUSE": role1.setRole("库管");break;
//                    case "USER":role1.setRole("普通用户");break;
//                    default:role1.setRole(roleName);break;
//                }
//                Role oldRole=roleRepository.findByRole(role1.getRole());
//                if(StringUtils.isEmpty(oldRole))
//                    roles.add(role1);
//                else roles.add(oldRole);
//            }
//            userModel.setRoles(roles);
//        }
        return userModel;
    }
    /**查询高管列表
     * 1.传入参数为空
     * 2.完成查询所有的高管的操作
     * @return
     */
    @Override
    public Map<String, Object> listApprover() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<Role>roles=new ArrayList<>();
        Role role=roleRepository.findByRole("ROLE_SENIOREXECUTIVE");
        roles.add(role);
        List<User> users= userRepository.findByRoles(roles);
        if(!users.isEmpty()){
//            List<UserModel> userModels=new ArrayList<>();
//            for (User user:users) {
//               userModels.add(UserToUserModel(user));
//            }
            modelMap.put("success",true);
            modelMap.put("Msg","获取高管列表数据成功");
            modelMap.put("listUser",users);
        }
        else {
            modelMap.put("success",false);
            modelMap.put("Msg","高管数据为空，获取高管数据失败");
        }
        return modelMap;
    }
}
