package com.examportal.service;

import com.examportal.dto.AddressDTO;
import com.examportal.dto.UserDTO;
import com.examportal.dto.UserDetailsDTO;
import com.examportal.exception.custom.*;
import jakarta.ws.rs.InternalServerErrorException;

public interface IUserService {

    public UserDTO registerNewUser(UserDTO userDTO) throws ResourceNotFoundException, UserServiceException, InternalServerException;

    public UserDTO getUserByUsername(String username) throws ResourceNotFoundException, InternalServerException, UserServiceException;

    public UserDTO getUserByEmail(String email) throws ResourceNotFoundException, InternalServerException, UserServiceException;

    public boolean isUsernameUnique(String username) throws InternalServerException, UserServiceException;

    public boolean isEmailUnique(String email) throws InternalServerException, UserServiceException;

    public boolean isPhoneNumberUnique(String phoneNumber) throws InternalServerException, UserServiceException;

    public void changePassword(String oldPassword, String newPassword) throws UnauthorisedException, InternalServerException, UserServiceException ;

    public void sendResetPasswordInstructions(String email) throws ResourceNotFoundException, InternalServerException, UserServiceException;

    public void verifyUserEmail(String email, String verificationToken) throws InvalidTokenException, InternalServerException, UserServiceException;








    public AddressDTO getAddress() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException;

    public void deleteUser() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException;

    public UserDetailsDTO getUserDetails() throws UnauthorisedException, ResourceNotFoundException, InternalServerException, UserServiceException;

}
