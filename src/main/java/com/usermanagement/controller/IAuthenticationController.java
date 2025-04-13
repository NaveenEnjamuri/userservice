package com.usermanagement.controller;

import com.usermanagement.entity.User;
import com.usermanagement.exception.ApiError;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Validated
@Api(value = "User Service Authentication APIs", tags = {"Auth APIs"})
public interface IAuthenticationController {

    @ApiIgnore
    @RequestMapping(value = "/authenticationSuccess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> authenticationSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception;

    @ApiIgnore
    @RequestMapping(value = "/authenticationFailure", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> authenticationFailure(@RequestParam(name = "logout", required = false) boolean logout,
                                                          @RequestParam(name = "message", required = false) String message, HttpServletRequest request, Http) ;

    @ApiIgnore
    @RequestMapping(value = "/403", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> accessDenied(@AuthenticationPrincipal User activeUser);

    @ApiIgnore
    @RequestMapping(value = "/logoutSuccess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> logoutSuccess(@RequestParam(name = "failure", required = false) boolean failure, HttpServletRequest request, HttpSesion )

    @GetMapping(value = "/verify-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiError> verifyAccount(@RequestParam(name = "email", required = true) String email, @RequestParam(name = "token", required = true))

}
