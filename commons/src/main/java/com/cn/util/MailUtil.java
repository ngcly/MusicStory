package com.cn.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 描述:
 * 邮件工具类
 *
 * @author chen
 * @create 2018-07-21 11:22
 */
@Component
public class MailUtil {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送html格式邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    /**
     * 完整版发送邮件
     * @param to 收件人
     * @param cc 抄送人
     * @param subject 主题
     * @param content 内容
     * @param fileName 附件名
     * @param filePath 附件地址
     * @throws MessagingException
     */
    public void sendEmail(String to,String cc,String subject,String content,String fileName,String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(subject);
        //开启html 格式
        helper.setText(content, true);
        //添加附件
        File file = new File(filePath);
        helper.addAttachment(fileName, file);
        mailSender.send(message);
    }

}
