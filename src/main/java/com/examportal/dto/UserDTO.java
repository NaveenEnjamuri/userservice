package com.examportal.dto;

import com.examportal.entity.Role;
import com.examportal.entity.UserProfile;
import com.examportal.entity.UserVerificationCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO {

    private Long userId;

    private String email;

    private String username;

    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    private String qualification;

    private boolean isAccountLocked;

    private boolean isEmailVerified;

    private boolean isPhoneNumberVerified;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date emailVerifiedTs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date phoneNumberVerifiedTs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date registeredTs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedTs;

    private RoleDTO roleDTO;

    public Map<String, String> validate() {
        Map<String, String> errorMap = new HashMap<>();
        if (StringUtils.isBlank(this.getUsername())) {
            errorMap.put("username", "Username shouldn't be empty");
        }
        if (StringUtils.isBlank(this.getEmail())) {
            errorMap.put("email", "Email shouldn't be empty");
        }
        if (StringUtils.isBlank(this.getPhoneNumber())) {
            errorMap.put("phoneNumber", "PhoneNumber shouldn't be empty");
        }
        if (StringUtils.isBlank(this.getQualification())) {
            errorMap.put("qualification", "Qualification shouldn't be empty");
        }
        if (StringUtils.isBlank(this.getPassword())) {
            errorMap.put("password", "Password shouldn't be empty");
        }
        if (StringUtils.isBlank(this.getConfirmPassword())) {
            errorMap.put("confirmPassword", "Password's didn't match");
        }
        if (this.getRoleDTO() == null || StringUtils.isBlank(this.getRoleDTO().getRoleName())) {
            errorMap.put("role", "Role is invalid.");
        }
        return errorMap;
    }

    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserProfile userProfile;

    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserVerificationCode userVerificationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
