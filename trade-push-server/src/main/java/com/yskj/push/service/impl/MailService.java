package com.yskj.push.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/1/18 13:12
 * @Version 1.0.0
 */
//@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

//    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String from, String toMail, String subject, String name, String content){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            String[] mailToArry = toMail.split(",");
            for(String mail : mailToArry){
                messageHelper.addTo(mail);
            }
            messageHelper.setText(content, true);
            javaMailSender.send(message);
            LOGGER.info("邮件发送成功");
        } catch (MessagingException e) {
            LOGGER.error("邮件发送异常：{}", e);
        }
    }
}
