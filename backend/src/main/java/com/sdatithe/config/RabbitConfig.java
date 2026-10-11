package com.sdatithe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "tithe.events";
    public static final String ROUTING_KEY = "payment.completed";
    public static final String EMAIL_QUEUE = "receipt.email";
    public static final String SMS_QUEUE = "receipt.sms";

    @Bean TopicExchange titheExchange() { return new TopicExchange(EXCHANGE, true, false); }

    @Bean Queue emailQueue() { return QueueBuilder.durable(EMAIL_QUEUE).build(); }

    @Bean Queue smsQueue() { return QueueBuilder.durable(SMS_QUEUE).build(); }

    @Bean Binding emailBinding() { return BindingBuilder.bind(emailQueue()).to(titheExchange()).with(ROUTING_KEY); }

    @Bean Binding smsBinding() { return BindingBuilder.bind(smsQueue()).to(titheExchange()).with(ROUTING_KEY); }

    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
