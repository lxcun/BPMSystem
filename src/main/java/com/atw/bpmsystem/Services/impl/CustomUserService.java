package com.atw.bpmsystem.Services.impl;


import com.atw.bpmsystem.Entities.User;
import com.atw.bpmsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 实现UserDetailsService返回用户
 */
public class CustomUserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       User user=userRepository.findByLoginName(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        System.out.println("s:"+s);
        System.out.println("username:"+user.getUsername()+";password:"+user.getPassword());
        return user;
    }
}
