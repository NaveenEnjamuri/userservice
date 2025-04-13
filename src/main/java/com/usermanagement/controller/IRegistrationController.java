package com.usermanagement.controller;

import com.usermanagement.dto.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@Api(value = "User Service Registration APIs", tags = {"Registration APIs"})
public interface IRegistrationController {

    @ApiOperation(value = "Registration Details - User registration by passing user details", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Returned on success along with the response body."),
            @ApiResponse(code = 401, message = "Returned when the caller is not authorized to make an API call."),
            @ApiResponse(code = 400, message = "Returned when the incorrect URL is passed."),
            @ApiResponse(code = 500, message = "Returned when request can not be handled.")
    })
    @PutMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) throws ResourceNotFoundException, UserServiceException, InternalServerException;
}
