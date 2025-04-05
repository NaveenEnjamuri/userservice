package com.examportal.controller;

import com.examportal.dto.UserProfileDTO;
import com.examportal.exception.custom.InternalServerException;
import com.examportal.exception.custom.ResourceNotFoundException;
import com.examportal.exception.custom.UnauthorisedException;
import com.examportal.exception.custom.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserController implements IUserController {

    @Override
    public ResponseEntity<Object> usernameExists(String username) throws UserServiceException, InternalServerException {
        return null;
    }

    @Override
    public ResponseEntity<Object> emailExists(String email) throws UserServiceException, InternalServerException {
        return null;
    }

    @Override
    public ResponseEntity<Object> phoneExists(String phone) throws UserServiceException, InternalServerException {
        return null;
    }

    @Override
    public ResponseEntity<Object> getUserDetails() throws UserServiceException, ResourceNotFoundException, InternalServerException, UnauthorisedException {
        return null;
    }

    @Override
    public ResponseEntity<Object> getProfile() throws InternalServerException, UserServiceException, ResourceNotFoundException, UnauthorisedException {
        return null;
    }

    @Override
    public ResponseEntity<Object> updateProfile(UserProfileDTO userProfileDTO) throws InternalServerException, UserServiceException, ResourceNotFoundException, UnauthorisedException {
        return null;
    }




    {
     return new ResponseEntity<>(map, HttpStatus.Ok);
}

    @Override
    public ResponseEntity<Object> getUserDetails() throws UserServiceException, ResourceNotFoundException, InternalServerException, UnauthorisedException {
        return new ResponseEntity<>(userService.getUserDetails(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getProfile() throws InternalServerException, UserServiceException, ResourceNotFoundException, UnauthorisedException {
        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }

}
