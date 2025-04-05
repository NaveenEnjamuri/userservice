package com.examportal.entity;

//import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "phone_number") })
@Data
@AllArgsConstructor
//@RequiredArgsConstructor
//@ToString
//@EqualsAndHashCode
public class User implements java.io.Serializable{
	
    private static final long serialVersionUID =6841555388320409449L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", unique = true, nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "qualification", nullable = false)
    private String qualification;

    @Column(name = "is_account_locked", nullable = false)
    private boolean isAccountLocked;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified;

    @Column(name = "is_phone_number_verified", nullable = false)
    private boolean isPhoneNumberVerified;

    @Column(name = "email_verified_ts", nullable = true)
    private Timestamp emailVerifiedTs;

    @Column(name = "phone_number_verified_ts", nullable = true)
    private Timestamp phoneNumberVerifiedTs;

    @Column(name = "registered_ts", nullable = false)
    private Timestamp registeredTs;

    @Column(name = "updated_ts", nullable = false)
    private Timestamp updatedTs;

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
