package com.usermanagement.client;


import com.usermanagement.dto.NotificationRequest;

public interface NotificationClient {
    void sendNotification(NotificationRequest request);
}

//import com.usermanagement.dto.EmailRequestDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;

//@Component
//@FeignClient(name = "notification-service", url = "https://exam2game.in/notification-service")
//public interface NotificationClient {
//
//
//    // event drivenpattern, messagequeue, Gateway, circuitbreaker
//
//    @PostMapping(value = "/email/send-by-template", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void sendByTemplate(@RequestBody EmailRequestDTO emailRequestDTO);
//
//    @PostMapping(value = "/email/send-by-body", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void sendByBody(@RequestBody EmailRequestDTO emailRequestDTO);
//}
