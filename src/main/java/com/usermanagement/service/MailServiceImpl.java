package com.usermanagement.service;

import com.usermanagement.client.NotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private NotificationClient notificationClient;

    @Async
    @Override
    public void sendEmail(List<String> toEmails, String fromEmail, String subject, String templateName, Object data) {
        try {
            notificationClient.sendByTemplate(EmailRequestDTO.builder()
                    .to(toEmails)
                    .from(fromEmail)
                    .subject(subject)
                    .templateName(templateName)
                    .data(data)
                    .build());

        } catch (Exception e) {
            log.error("error sending email for template: " + templateName, e);
        }
    }

    @Async
    @Override
    public void sendEmail(List<String> toEmails, String fromEmail, String subject, Object data) {
        try {
            notificationClient.sendByBody(EmailRequestDTO.builder()
                    .to(toEmails)
                    .from(fromEmail)
                    .subject(subject)
                    .data(data)
                    .build());
        } catch (Exception e) {
            log.error("error sending email for: " + toEmails, e);
        }
    }

}
