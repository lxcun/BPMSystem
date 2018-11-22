package com.atw.bpmsystem.Services.impl;

import com.atw.bpmsystem.Common.RequestUtil;
import com.atw.bpmsystem.Services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MailServiceImpl implements MailService{
    @Autowired
   private JavaMailSender mailSender;

    /**发送邮件
     * 1.传入参数为接收者的recipient1，邮件标题title，邮件内容text
     * @param recipient1
     * @param title
     * @param text
     * @return
     */
    @Override
    public Object sendEmail(String recipient1,String title,String text) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
           if(isEmail(recipient1)){
               helper.setFrom("atwmanage@qq.com");
               helper.setTo(recipient1);
               helper.setSubject(title);
               StringBuffer sb = new StringBuffer();
               RequestUtil requestUtil=new RequestUtil();
               String LINK=requestUtil.getLocalhostIP()+":8088";
               log.error("邮箱发送链接"+LINK);
               sb.append("<h1>"+title+"</h1>")
                  .append("<p >"+text+"</p>")
                  .append("<p>请点击链接</p>")
                  .append("<a>"+LINK+"</a>")
                  .append("<p>进入系统</p>");
               helper.setText(sb.toString(),true);
               mailSender.send(message);
               return "sucesss";
           }else {
               log.error("邮件发送失败");
               return "error";
           }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

}
