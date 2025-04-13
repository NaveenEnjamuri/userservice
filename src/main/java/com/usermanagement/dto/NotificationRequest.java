package com.usermanagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private String toEmail;
    private String subject;
    private String body;
}
