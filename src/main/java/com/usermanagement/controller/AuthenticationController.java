package com.usermanagement.controller;

import com.usermanagement.entity.User;
import com.usermanagement.exception.ApiError;
import com.usermanagement.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@Slf4j
public class AuthenticationController implements IAuthenticationController, AuthApi{

    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<ApiError> authenticationSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return new ResponseEntity<>(ApiError.builder()
                    .message("Login was Successful")
                    .status(HttpStatus.OK)
                    .currentTime(LocalDateTime.now())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(Messages.SOMETHING_WENT_WRONG, e);
            return new ResponseEntity<>(ApiError.builder()



            )
        }
    }

    @Override
    public ResponseEntity<ApiError> authenticationFailure(boolean logout, String message, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<ApiError> accessDenied(User activeUser) {
        return null;
    }

}
