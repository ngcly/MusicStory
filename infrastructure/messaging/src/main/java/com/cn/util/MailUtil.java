package com.cn.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * 描述:
 * 邮件工具类
 * @author ngcly
 * @since 2018-07-21 11:22
 */
@Slf4j
@Component
public class MailUtil {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

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
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
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
     * 异步发送邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param templateName 模板文件名
     * @param context 注入模板内容
     */
    @Async
    public void sendAsyncMail(String to,String subject,String templateName, Dict context) {
        try {
            sendTemplateMail(to,subject,templateName,context);
        }catch (Exception e){
            log.error("邮件发送失败",e);
        }
    }

    /**
     * 根据邮件模板发送
     * @param to 收件人
     * @param subject 邮件主题
     * @param templateName 模板文件名
     * @param context 注入模板内容
     * @throws MessagingException 信息异常
     */
    public void sendTemplateMail(String to,String subject,String templateName, Dict context) throws MessagingException {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate(templateName);
        String result = template.render(context);
        sendHtmlMail(to,subject,result);
    }

    /**
     * 完整版发送邮件
     * @param to 收件人
     * @param cc 抄送人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否为html格式
     * @param files 附件列表Map（附件名:附件地址）
     * @throws MessagingException 信息异常
     */
    public void sendEmail(String[] to, String[] cc, String subject, String content, boolean isHtml, Map<String,String> files) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(subject);
        //开启html 格式
        helper.setText(content, isHtml);
        File file;
        for(Map.Entry<String, String> entry : files.entrySet()){
            //添加附件
            file= FileUtil.file(entry.getValue());
            helper.addAttachment(entry.getKey(), file);
        }
        mailSender.send(message);
    }
}
