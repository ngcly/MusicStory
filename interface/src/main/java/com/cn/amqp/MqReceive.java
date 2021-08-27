package com.cn.amqp;

import com.cn.UserRelatedService;
import com.cn.UserService;
import com.cn.config.RabbitConfig;
import com.cn.entity.News;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 队列消费
 * @author ngcly
 */
@Component
@AllArgsConstructor
public class MqReceive {
    private static final Logger log = LoggerFactory.getLogger(MqReceive.class);

    private final UserService userService;
    private final UserRelatedService userRelatedService;

    @RabbitListener(queues = {RabbitConfig.DELAY_QUEUE})
    public void consumeDelay(Long userId) {
        log.info("[执行清除过期未激活账号] - [{}]", userId);
        userService.delUnActiveUser(userId);
    }

    @RabbitListener(queues = {RabbitConfig.DELAY_QUEUE})
    public void consumeNotify(News news) {
        log.info("[消息通知] - [{}]", news);
        userRelatedService.notifyUser(news);
    }
}
