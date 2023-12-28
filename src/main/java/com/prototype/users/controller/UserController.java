package com.prototype.users.controller;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.exception.UserException;
import com.prototype.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserOutputDTO> registerUser(@RequestBody UserInputDTO userInput) {
        try {
            // Register the user
            UserOutputDTO userOutputDTO = userService.register(userInput);
            return ResponseEntity.status(HttpStatus.CREATED).body(userOutputDTO);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new UserOutputDTO(userInput.name(), "", LocalDateTime.now(), e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<UserOutputDTO> validateToken(@RequestBody UserInputDTO userInput) {
        try {
            // Validate a token and return
            var userOutputDTO = userService.validate(userInput.token());
            return ResponseEntity.status(HttpStatus.OK).body(userOutputDTO);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new UserOutputDTO(userInput.name(), userInput.token(), LocalDateTime.now(), e.getMessage()));
        }
    }
}