package com.atw.bpmsystem.Services;

import com.atw.bpmsystem.Entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void verifyUser() throws Exception {
//        User user=new User();
//        user.setName("lxc");
//        user.setPassword("123");
//        userService.verifyUser(user).get("success");
    }

    @Test
    public void registerUser() throws Exception {
//        User user=new User();
//        user.setLoginName("lxc");
//        user.setPassword("123");
//       // userService.registerUser(user);
//        user = new User();
//        user.setLoginName("pcx");
//        user.setPassword("123");
//       // userService.registerUser(user);
    }

    @Test
    public void deleteUser() throws Exception {
    }

    @Test
    public void modifyUser() throws Exception {
    }

    @Test
    public void listUser() throws Exception {
    }

}