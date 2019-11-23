package com.cn.amqp;

import com.cn.config.RabbitConfig;
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

    @RabbitListener(queues = {RabbitConfig.ACTIVE_QUEUE})
    public void consume(Map<String,String> map) {
        log.info("[Rabbit listener 监听的消息] - [{}]", map);
        try {
            mailUtil.sendHtmlMail(map.get("to"),map.get("subject"),map.get("context"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
