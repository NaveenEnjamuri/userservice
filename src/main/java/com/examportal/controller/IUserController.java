package com.examportal.controller;

import com.examportal.dto.UserProfileDTO;
import com.examportal.exception.custom.InternalServerException;
import com.examportal.exception.custom.ResourceNotFoundException;
import com.examportal.exception.custom.UnauthorisedException;
import com.examportal.exception.custom.UserServiceException;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Api(value = "User Service APIs", tags = {"User APIs"})
public interface IUserController {

    @GetMapping(value = "/username-exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> usernameExists(@RequestParam(name = "username", required = true) String username) throws UserServiceException, InternalServerException;

    @GetMapping(value = "/email-exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> emailExists(@RequestParam(name = "email", required = true) String email) throws UserServiceException, InternalServerException;

    @GetMapping(value = "/phone-exists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> phoneExists(@RequestParam(name = "phone", required = true) String phone) throws UserServiceException, InternalServerException;

    @GetMapping(value = "/user-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserDetails() throws UserServiceException, ResourceNotFoundException, InternalServerException, UnauthorisedException;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProfile() throws InternalServerException, UserServiceException, ResourceNotFoundException, UnauthorisedException;

    @PostMapping(value = "/update-profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProfile(@RequestBody UserProfileDTO userProfileDTO) throws InternalServerException, UserServiceException, ResourceNotFoundException, UnauthorisedException;

}
