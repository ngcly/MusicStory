package com.cn.listener;

import com.cn.relation.UserRelatedService;
import com.cn.user.UserService;
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
public class MqListener {
    private static final Logger log = LoggerFactory.getLogger(MqListener.class);

    private final UserService userService;
    private final UserRelatedService userRelatedService;

    @RabbitListener(bindings = @org.springframework.amqp.rabbit.annotation.QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue(name = RabbitConfig.DELAY_QUEUE, durable = "true"),
            exchange = @org.springframework.amqp.rabbit.annotation.Exchange(name = RabbitConfig.DELAY_EXCHANGE, type = "x-delayed-message", arguments = {
                    @org.springframework.amqp.rabbit.annotation.Argument(name = "x-delayed-type", value = "direct")
            }),
            key = RabbitConfig.DELAY_ROUTING_KEY
    ))
    public void consumeDelay(Long userId) {
        log.info("[执行清除过期未激活账号] - [{}]", userId);
        userService.delUnActiveUser(userId);
    }

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(name = RabbitConfig.NOTIFY_QUEUE, durable = "true"))
    public void consumeNotify(News news) {
        log.info("[消息通知] - [{}]", news);
        userRelatedService.notifyUser(news);
    }
}
