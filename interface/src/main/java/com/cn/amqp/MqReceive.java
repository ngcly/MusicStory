package com.cn.amqp;

import com.cn.UserService;
import com.cn.config.RabbitConfig;
import com.cn.entity.News;
import com.cn.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ngcly
 */
@Component
public class MqReceive {
    private static final Logger log = LoggerFactory.getLogger(MqReceive.class);

    @Autowired
    MailUtil mailUtil;
    @Autowired
    UserService userService;

    @RabbitListener(queues = {RabbitConfig.ACTIVE_QUEUE})
    public void consume(Map<String,String> map) {
        log.info("[注册激活邮件发送] - [{}]", map);
        try {
            mailUtil.sendHtmlMail(map.get("to"),map.get("subject"),map.get("context"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = {RabbitConfig.DELAY_QUEUE})
    public void consumeDelay(String userId) {
        log.info("[执行过期未激活账号] - [{}]", userId);
        userService.delUnActiveUser(userId);
    }

    @RabbitListener(queues = {RabbitConfig.DELAY_QUEUE})
    public void consumeNotify(News news) {
        log.info("[消息通知] - [{}]", news);
        userService.notifyUser(news);
    }
}
