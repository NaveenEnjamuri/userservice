package com.examportal.controller;

import com.examportal.dto.UserDTO;
import com.examportal.exception.custom.InternalServerException;
import com.examportal.exception.custom.ResourceNotFoundException;
import com.examportal.exception.custom.UserServiceException;
import com.examportal.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class RegistrationController implements IRegistrationController {

    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) throws ResourceNotFoundException, UserServiceException, InternalServerException {
        Map<String, String> errorMap = userDTO.validate();
        if (!userService.isEmailUnique(userDTO.getEmail())) {
            errorMap.put("email", "An account with this email is already registered with us. If you forgot the Password, reset by using forgot password.");
        }
        if (!userService.isPhoneNumberUnique(userDTO.getPhoneNumber())) {
            errorMap.put("phoneNumber", "This phone number is already associated with one of the existing accounts. Please use another one.");
        }
        if (!userService.isUsernameUnique(userDTO.getUsername())) {
            errorMap.put("username", "An account with this username is already registered with us. If you forgot the Password, reset by using forgot password.");
        }
        if (!errorMap.isEmpty()) {
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.registerNewUser(userDTO), HttpStatus.CREATED);

        //REST API - GET,POST,PUT,OPTIONS,DELETE

        //POST - if you want to create new resource
        //PUT - if you want to update the resource - modify the user data
        //GEt - if you want to fetch the user information from database
    }
}
