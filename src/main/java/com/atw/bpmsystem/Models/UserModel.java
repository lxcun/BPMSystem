package com.atw.bpmsystem.Models;

import com.atw.bpmsystem.Entities.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class UserModel {
    private Integer id;
    private String loginName;                //登录名
    private String password;                //密码
    private Map<Integer, String> roles;
    private String operation;               //部门
    private String position;                //职务
    private String name;                   //姓名
    private String phone;                  //电话
    private String email;                  //邮箱
}
