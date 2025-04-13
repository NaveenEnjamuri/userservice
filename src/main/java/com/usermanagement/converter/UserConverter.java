package com.usermanagement.converter;

import com.usermanagement.dto.UserDTO;
import com.usermanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends MasterConverter{

    public User convert(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
