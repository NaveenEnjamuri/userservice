package com.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "USER_PROFILE")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class UserProfile implements java.io.Serializable {

    private static final long serialVersionUID = -3123020265348424874L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id", unique = true, nullable = false)
    private Long userProfileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "alternate_phone_number", nullable = true, length = 12)
    private String alternatePhoneNumber;

    @Column(name = "alternate_email", nullable = true)
    private String alternateEmail;

    @Column(name = "gender", nullable = true, length = 6)
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "dob", length = 10, nullable = true)
    private Date dob;

    @Column(name = "address_line1", nullable = true)
    private String addressLine1;

    @Column(name = "address_line2", nullable = true)
    private String addressLine2;

    @Column(name = "address_line3", nullable = true)
    private String addressLine3;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "state", nullable = true)
    private String state;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "pincode", nullable = true)
    private int pincode;
}
