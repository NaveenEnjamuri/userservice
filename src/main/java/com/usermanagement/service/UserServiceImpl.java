package com.usermanagement.service;

import com.usermanagement.configuration.authentication.SecurityParams;
import com.usermanagement.configuration.publisher.RabbitMQPublisher;
import com.usermanagement.constants.Messages;
import com.usermanagement.converter.UserConverter;
import com.usermanagement.converter.UserProfileConverter;
import com.usermanagement.dto.UserDTO;
import com.usermanagement.dto.UserDetailsDTO;
import com.usermanagement.dto.UserProfileDTO;
import com.usermanagement.dto.VerifyMailDTO;
import com.usermanagement.dto.AddressDTO;
import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.entity.UserProfile;
import com.usermanagement.entity.UserVerificationCode;
import com.usermanagement.exception.custom.InternalServerException;
import com.usermanagement.exception.custom.InvalidTokenException;
import com.usermanagement.exception.custom.ResourceNotFoundException;
import com.usermanagement.exception.custom.UnauthorisedException;
import com.usermanagement.exception.custom.UserServiceException;
import com.usermanagement.repository.IRoleRepository;
import com.usermanagement.repository.IUserProfileRepository;
import com.usermanagement.repository.IUserRepository;
import com.usermanagement.repository.IUserVerificationCodeRepository;
//import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
//import org.glassfish.jaxb.core.v2.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Slf4j
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserVerificationCodeRepository userVerificationCodeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserProfileConverter userProfileConverter;

    @Autowired
    private SecurityParams securityParams;

    @Autowired
    private IMailService mailService;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    private @Value("${email}") String fromMail;

    private @Value("${verify.link}") String verifyLink;

    private @Value("${register.mail.subject}") String registerSubject;

    private @Value("${register.success.template}") String registerTemplateName;

    private @Value("${reset.mail.subject}") String resetPasswordEmailSubject;

    private @Value("${reset.template}") String  resetPasswordEmailTemplate;

    private @Value("${reset.link}") String resetPasswordLink;

    @Override
    public UserDTO registerNewUser(UserDTO userDTO) throws ResourceNotFoundException, UserServiceException, InternalServerException {
        try {
            User user = userConverter.convert(userDTO);

            //encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            //set user verification code
            UserVerificationCode userVerificationCode = new UserVerificationCode();
            userVerificationCode.setInitialCreatedTs(Timestamp.from(Instant.now()));
            userVerificationCode.setSignupVerificationCode(UUID.randomUUID().toString());
            userVerificationCode.setSignupVerificationCodeCreatedTs(Timestamp.from(Instant.now()));
            userVerificationCode.setSignupVerificationCodeUsed(false);
            user.setUserVerificationCode(userVerificationCode);
            userVerificationCode.setUser(user);

            Optional<Role> role = roleRepository.findByRoleName(userDTO.getRoleDTO.getRoleName());
            if (!role.isPresent()) {
                log.error("Invalid role selected {} for", userDTO.getEmail());
                throw new ResourceNotFoundException(Messages.INVALID_ROLE);
            }
            user.setRegisteredTs(Timestamp.from(Instant.now()));
            user.setRole(role.get());

            User savedEntity = userRepository.save(user);
            // savedEntity.setPhoneNumber("7892221");
            //userRespository.save(user);

            //collections, exceptions, oops,multithreading - next round
            //sorting,searching best way interms of performance and storage


            // dbconnection,
            // begin transaction,
            // commit transaction if it is successful or rollback any failure
            // different states of object in hibernate - transient, persistent, detached
            // hibernate, db operation

            RestTemplate restTemplate = new RestTemplate();
//            HttpEntity<User> requestEntity = null;
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/notification-service/controller1/{message}", HttpMethod.GET, null,String.class, "null");

            log.info("response status code::{}", response.getStatusCode());
            log.info("response body::{}", response.getBody());


            rabbitMQPublisher.publishMessage("registerEmailQueue", "registered in database");

            //send email verification
            mailService.sendEmail(Collections.singletonList(userDTO.getEmail()),
                    fromMail, registerSubject, registerTemplateName,
                    VerifyMailDTO.builder()
                            .username(userDTO.getUsername())
                            .email(userDTO.getEmail())
                            .verifyLink(verifyLink + userDTO.getEmail() + "&token=" + userVerificationCode.getSignupVerificationCode())
                            .build()
            );
            return  userConverter.convert(user);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e)  {
            log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword)
        throws UnauthorisedException, InternalServerException, UserServiceException {

    }

    @Override
    public UserDTO getUserByUsername(String username) throws ResourceNotFoundException, InternalServerException, UserServiceException {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (!optionalUser.isPresent()) {
                throw new ResourceNotFoundException("Username : " + username + "not found");
            }
            return userConverter.convert(optionalUser.get());
        } catch (ResourceNotFoundException e) {
            A.log.error(e.getmessage(), e);
            throw e;
        } catch (DataAccessException e){
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
        return null;
    }

    @Override
    public UserDTO getUserByEmail(String email) throws ResourceNotFoundException, InternalSeverException, UserServiceException {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (!optionalUser.isPresent()) {
                throw new ResourceNotFoundException("Email : " + email + " not found!");
            }
            return userConverter.convert(optionalUser.get());
        } catch (ResourceNotFoundException e) {
            A.log.error(e.getMessage(), e);
            throw e;
        } catch (DataAccessException e) {

        }

    }



    @Override
    public void sendResetPasswordInstructions(String email) throws ResourceNotFoundException, InternalServerException, UserServiceException {
        try {
            String uuid = UUID.randomUUID().toString();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (!optionalUser.isPresent()) {
                throw new ResourceNotFoundException("Email : " + email + "not found!");
            }

            if (!optionalUser.get().isEmailVerified()) {
                mailService.sendEmail(
                        Collections.singletonList(optionalUser.get().getEmail()), fromMail,
                        registerSubject, registerTemplateName, VerifyMailDTO.builder()
                                .username(optionalUser.get().getUsername())
                                .email(optionalUser.get().getEmail())
                                .verifyLink(verifyLink+optionalUser.get().getEmail()+"&token"+optionalUser.get().getUserVerificationCode().getSignupVerificationCode)


                )
            }











        }
    }






    @Override
    public boolean isUsernameUnique(String username) throws InternalServerException, UserServiceException {
        try {
            return userRepository.countByUsername(username) == 0;
        } catch (DataAccessException e) {
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }

    }

    @Override
    public boolean isEmailUnique(String Email) throws InternalServerException, UserServiceException {
//        return false;
        try {
            return userRepository.countByEmail(email) == 0;
        } catch (DataAccessException e) {
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public boolean isPhoneNumberUnique(String phoneNumber) throws InternalServerException, UserServiceException {
        try {
            return userRepository.countByPhoneNumber(phoneNumber) == 0;
        } catch (DataAccessException e) {
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
//        return false;
    }

    @Override
    public void resetPassword(String token, String newPassword) throws InvalidTokenException, InternalServerException, UserServiceException {
        try {
            Optional<UserVerificationCode> optionalUserVerificationCode = userVerificationCodeRepository.findByPasswordResetCode(token);
            if (!optionalUserVerificationCode.isPresent()) {
                throw new InvalidTokenException("Token not found!");
            } if (optionalUserVerificationCode.get().isPasswordResetCodeUsed()) {
                throw new InvalidTokenException("Token not Invalid!");
            }
            User user = optionalUserVerificationCode.get().getUser();
            if (user != null) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setUpdatedTs(Timestamp.from(Instant.now()));
                user.getUserVerificationCode().setPsswordResetCodeUsed(true);
                user.setAccountLocked(false);
                userRepository.save(user);
            } else {
                throw new InvalidTokenException("Your token is Invalid. Please use the valid token or request for it by using forgot password.");
            }
        } catch (InvalidTokenException e) {
            A.log.error(e.getMessage(), e);
            throw e;
        }  catch (DataAccessException e) {
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void verifyUserEmail(String email, String verificationToken) throws InvalidTokenException, InternalServerException, UserServiceException {
        try {
            Optional<UserVerificationCode> optionalUserVerificationCode = userVerificationCodeRepository.findBySignupVerificationCode(verificationToken);
            if (!optionalUserVerificationCode.isPresent()) {
                throw new InvalidTokenException("It seems like verification link has broken or invalid. " +
                        "Please verify your email again or use forgot password to generate another link.");
            }
            User user = optionalUserVerificationCode.get().getUser();
            if (!user.getEmail().equalsIgnoreCase(email)) {
                throw new InvalidTokenException("It seems like verification link has broken or invalid. " +
                        "Please verify your email again or use forgot password to generate another link.");
            }
            if (optionalUserVerificationCode.get().isSignupVerificationCodeUsed()) {
                throw new InvalidTokenException("It seems like verification link has broken or invalid. " +
                        "Please verify your email again or use forgot password to generate another link.");
            }
            if (optionalUserVerificationCode.get().getUser().isEmailVerified()) {
                throw new UserServiceException("Your email has already been verified. You can login to your account. " +
                        "If you are facing any issues with the login, Please contact us!");
            }
            user.setEmailVerifiedTs(Timestamp.from(Instant.now()));
            user.setEmailVerified(true);
            user.getUserVerificationCode().setSignupVerificationCodeUsed(true);

            userRepository.save(user);
        } catch (InvalidTokenException e) {
            A.log.error(e.getMessage(), e);
            throw e;
        } catch (DataAccessException e) {
            A.log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public UserProfileDTO updateUserProfile(UserProfileDTO userProfileDTO) throws UnauthorisedException, ResourceNotFoundException, InternalServerException {
        try {
            Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUsername(securityParams.getMyUsername());
            if (!optionalUserProfile.isPresent()) {
                Optional<User> optionalUser = userRepository.findByUsername(securityParams.getMyUsername());
                if (!optionalUser.isPresent()) {
                    throw new ResourceNotFoundException(Messages.INVALID_USER);
                }
                UserProfile userProfile = userProfileConverter.convert(userProfileDTO);
                userProfile.setUser(optionalUser.get());
                return userProfileConverter.convert(userProfileRepository.save(UserProfile));
            }
            optionalUserProfile.get().setFirstName(userProfileDTO.getFirstName());
            optionalUserProfile.get().setLastName(userProfileDTO.getLastName());
            optionalUserProfile.get().setAlternatePhoneNumber(userProfileDTO.getAlternatePhoneNumber());
            optionalUserProfile.get().setAlternateEmail(userProfileDTO.getAlternateEmail());
            optionalUserProfile.get().setGender(userProfileDTO.getGender());
            optionalUserProfile.get().setDob(new java.sql.Date(userProfileDTO.getDob().getTime()));
            optionalUserProfile.get()






        }
    }











    @Override
    public AddressDTO getAddress() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException {
        try {


            {

                throw new ResourceNotFoundException("Address is not created.");
            }
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setAddressLine1(optionalUserProfile.get().getAddressLine1());
            addressDTO.setAddressLine2(optionalUserProfile.get().getAddressLine2());
            addressDTO.setAddressLine3(optionalUserProfile.get().getAddressLine3());
            addressDTO.setCity(optionalUserProfile.get().getCity());
            addressDTO.setState(optionalUserProfile.get().getState());
            addressDTO.setCountry(optionalUserProfile.get().getCountry());
            addressDTO.setPincode(optionalUserProfile.get().getPincode());
            return addressDTO;
        } catch (UnauthorisedException e) {
            A.log.error(Messages.UNAUTHORISED, e);
            throw e;
        } catch (ResourceNotFoundException e) {

        }
        catch ( ) {

            throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
        } catch (Exception e) {
            A.log.error(Messages.SOMETHING_WENT_WRONG, e);
            throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);        }
    }

//        return null;
//}

@Override
public void changePassword(String oldPassword, String newPassword)
        throws UnauthorisedException, InternalServerException, UserServiceException {
    try {
        Optional<User> optionalUser = userRepository.findByUsername(securityParams.getMyusername());
        if (optionalUser.isPresent()) {
            if (!passwordEncoder.matches(oldPassword, optionalUser.get().getPassword())){
                throw new UserServiceException(Messages.PASSWORDS_NOT_MATCHED);
            }
            optionalUser.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(optionalUser.get());
        } else{
            throw new UnauthorisedException(Messages.SESSION_EXPIRED);
        }
    } catch (UnauthorisedException e){
        log.error(Messages.UNAUTHORISED, e);
        throw e;
    } catch (DataAccessException e) {
        log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
    }
}

@Override
public void deleteUser() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException {
    try {
        Optional<User> optionalUser = userRepository.findByUsernameOrEmailOrPhoneNumber(securityParams.getMyUsername());
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(Messages.INVALID_USER);
        }
        userRepository.delete(optionalUser.get());
    } catch (UnauthorisedException e) {
        log.error(Messages.UNAUTHORISED, e);
        throw e;
    } catch (ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        throw e;
    } catch (DataAccessException e) {
        log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
        throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
    } catch (Exception e) {
        log.error(Messages.SOMETHING_WENT_WRONG, e);
        throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);        }
}






@Override
public UserDetailsDTO getUserDetails() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException {
    try {
        Optional<User> optionalUser = userRepository.findByUsername(securityParams.getMyUsername());
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(Messages.INVALID_USER);
        }

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(String.valueOf(optionalUser.get().getUserId()));
        userDetailsDTO.setEmail(optionalUser.get().getEmail());
        userDetailsDTO.setUsername(optionalUser.get().getUsername());
        userDetailsDTO.setPhoneNumber(optionalUser.get().getPhoneNumber());
        userDetailsDTO.setQualificaton(optionalUser.get().getQualification());
        userDetailsDTO.setRole(securityParams.getMyRoles()[0]);
        if (optionalUser.get().getUserProfile() != null) {
            userDetailsDTO.setFirstName(optionalUser.get().getUserProfile().getFirstName());
            userDetailsDTO.setLastName(optionalUser.get().getUserProfile().getLastName());
        }
        return userDetailsDTO;
    } catch (UnauthorisedException e) {
        log.error(Messages.UNAUTHORISED, e);
        throw e;
    } catch (ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        throw e;
    } catch (DataAccessException e) {
        log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
        throw new InternalServerException(Messages.UNABLE_TO_ACCESS_DATA);
    } catch (Exception e) {
        log.error(Messages.SOMETHING_WENT_WRONG, e);
        throw new UserServiceException(Messages.SOMETHING_WENT_WRONG);
    }
}

}


