package com.prototype.users.service;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.exception.UserException;

public interface UserService {
    UserOutputDTO register(UserInputDTO userInput) throws UserException;
    UserOutputDTO validate(String token) throws UserException;
    void deRegister(String token) throws UserException;


}
