package com.examportal.configuration.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessage(String queue, String message) {
        rabbitTemplate.convertAndSend(queue, message);
    }
}

