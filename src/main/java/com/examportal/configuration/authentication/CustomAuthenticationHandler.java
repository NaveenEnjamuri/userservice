package com.examportal.configuration.authentication;

import com.examportal.dto.VerifyMailDTO;
import com.examportal.entity.Role;
import com.examportal.entity.User;
import com.examportal.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.*;

@Component(value = "authenticationProvider")
@Slf4j
public class CustomAuthenticationHandler implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationHandler.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IMailService mailService;

    private @Value("${email}") String fromMail;

    private @Value("${verify.link}") String verifyLink;

    private @Value("${register.mail.subject}") String registerSubject;

    private @Value("${register.success.template}") String registerTemplateName;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Optional<User> optionalUser = userRepository.findByUsernameOrEmailOrPhoneNumber(authentication.getName());
            if (!optionalUser.isPresent()) {
                throw new BadCredentialsException("Invalid Username!");
            }

            if (!optionalUser.get().isEmailVerified()) {
                try {
                    mailService.sendEmail(Collections.singletonList(optionalUser.get().getEmail()), fromMail,
                            registerSubject, registerTemplateName, VerifyMailDTO.builder()
                                    .username(optionalUser.get().getUsename())
                                    .email(optionalUser.get().getEmail())
                                    .verifyLink(verifyLink+optionalUser.get().getEmail()+"&token"+optionalUser.get().getUserVerificationCode().getSignupVerification)
                                     );
                } catch (Exception e) {
                    log.error("Exception while sending Email Verification Email", e);
                }
                throw new BadCredentialsException("Your email is not verified, please check your email for instructions.");
            }
            if (optionalUser.get().isAccountLocked()) {
                throw new BadCredentialsException("Your account got locked! Please use forgot password for password recovery");
            }

            if (!passwordEncoder.matches(authentication.getCredentials().toString(), optionalUser.get().getPassword())) {
                throw new BadCredentialsException("Wrong Password!");
            }

            Set<Role> roles = new HashSet<>();
            roles.add(optionalUser.get().getRole());
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(optionalUser.get().getUsername
                    !optionalUser.get().isAccountLocked(), true, true,
                     true, buildUserAuthority(roles));
            return new UsernamePasswordAuthenticationToken(user,
                    user.getPassword(), buildUserAuthority(roles));

        } finally {
            log.debug("Exiting from {}", this.getClass().getName());
        }

    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Build user's authorities
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new ArrayList<>(authorities);
    }
}
