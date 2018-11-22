package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Services.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {
    @Autowired
    MailService mailService;
    @Test
    public void sendEmail() throws Exception {
//        String title="测试邮件";
//     String text="<h2>Hi，你好</h2></br>";
//     mailService.sendEmail("516333085@qq.com",title,text);
//     String s="";
    }

}