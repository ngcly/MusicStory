package com.cn.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author ngcly
 * @version V1.0
 * @since 2021/8/14 18:48
 */
class IndexControllerTest {

    @Test
    void defaultCaptcha() {
        ICaptcha captcha = CaptchaUtil.createGifCaptcha(116, 36, 4);
        try (FileOutputStream out = new FileOutputStream("d:/gif.gif")){
            captcha.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.isTrue(captcha.verify(captcha.getCode()));
    }

    @Test
    void jwtTest() {
        final Date now =new Date();
        byte[] secret = SecureUtil.md5("ssss").getBytes();
        String token = JWT.create()
                .setSubject("cly")
                .setIssuedAt(now)
                .setExpiresAt(DateUtil.offset(now, DateField.SECOND,3))
                .setKey(secret)
                .sign();
        System.out.println(token);
        JWT jwt = JWTUtil.parseToken(token);
        Assert.isTrue(JWT.of(token).setKey(secret).validate(0L));
        String username = jwt.getPayloads().get(JWT.SUBJECT,String.class);
        Date date = jwt.getPayloads().getDate(JWT.ISSUED_AT);
        LocalDateTime issuedAt = DateUtil.toLocalDateTime(date);
        System.out.println(username);
        System.out.println(issuedAt);
    }

    @Test
    void test() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        System.out.println(runtime.availableProcessors());
        System.out.println(runtime.maxMemory());
        Scanner scanner = new Scanner(new ByteArrayInputStream("ssss".getBytes()));

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        Assertions.assertNotNull(scanner);
        if(scanner.hasNext()){
            String s= scanner.next();
            writer.write(s.getBytes());
            printStream.print(s);
        }
        System.out.println(writer);
        System.out.println(printStream);

    }

}