package com.usermanagement.client;

import com.usermanagement.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.usermanagement.constants.UserServiceConstants.NOTIFICATION_QUEUE;

@Service
@RequiredArgsConstructor
public class NotificationClientImpl implements NotificationClient {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNotification(NotificationRequest request) {
        rabbitTemplate.convertAndSend(NOTIFICATION_QUEUE, request);
    }
}
