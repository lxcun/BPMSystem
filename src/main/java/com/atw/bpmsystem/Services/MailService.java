package com.atw.bpmsystem.Services;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    /**发送邮件
     * 1.传入参数为接收者的recipient1，邮件标题title，邮件内容text
     * @param recipient1
     * @param title
     * @param text
     * @return
     */
    Object sendEmail(String recipient1,String title,String text);
}
