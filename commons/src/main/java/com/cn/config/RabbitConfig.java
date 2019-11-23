package com.cn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 * @author ngcly
 */
@Configuration
@EnableRabbit
public class RabbitConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitConfig.class);

    /**队列名称*/
    public static final String ACTIVE_QUEUE = "music-story";

    /**
     * 配置摸板（非必要 测试延迟队列方便直观）
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

    /**
     * rabbitmq 默认将信息对象按照jdk序列化 此处改为json序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**默认队列 该队列会自动被 rabbitmq 绑定到默认的交换机上*/
    @Bean
    public Queue activeQueue() {
        // 第一个是 QUEUE 的名字,第二个是消息是否需要持久化处理
        return new Queue(ACTIVE_QUEUE, true);
    }

}
