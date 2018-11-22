package com.atw.bpmsystem.Controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private  UserController userController;
//    @Test
//    public void currentUser() throws Exception {
//        String name=userController.currentUser();
//        System.out.println(name);
//    }

}