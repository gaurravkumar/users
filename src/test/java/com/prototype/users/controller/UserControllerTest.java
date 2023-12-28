package com.prototype.users.controller;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.exception.UserException;
import com.prototype.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private UserController userController;

    @Mock
    BindingResult bindingResult;

    UserOutputDTO userOutputDTO;
    UserInputDTO userInputDTO;

    @BeforeEach
    void setUp() {
        userInputDTO = new UserInputDTO("name", "email", "password", null);
        userOutputDTO = new UserOutputDTO("name", "token", LocalDateTime.now(), null);
    }

    @Test
    void registerUser() {
        when(userServiceMock.register(any(UserInputDTO.class))).thenReturn(userOutputDTO);
        ResponseEntity<UserOutputDTO> userOutputDTOResponseEntity = userController.registerUser(userInputDTO, bindingResult);
        assertNotNull(userOutputDTOResponseEntity);
        assertTrue(userOutputDTOResponseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    void registerUserFieldsEmptyWhileRegistrationErrorResponse() {
        FieldError fieldError = new FieldError("userInputDTO","name","Blank prohibited");
        FieldError fieldError2 = new FieldError("userInputDTO","email","Blank prohibited");
        FieldError fieldError3 = new FieldError("userInputDTO","password","Blank prohibited");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError,fieldError2,fieldError3));
        ResponseEntity<UserOutputDTO> userOutputDTOResponseEntity = userController.registerUser(userInputDTO, bindingResult);
        assertNotNull(userOutputDTOResponseEntity);
        assertTrue(userOutputDTOResponseEntity.getBody().error().contains("Blank prohibited"));
        assertTrue(userOutputDTOResponseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    void registerUserWhenUserServiceThrowException() {
        when(userServiceMock.register(any(UserInputDTO.class))).thenThrow(UserException.class);
        ResponseEntity<UserOutputDTO> userOutputDTOResponseEntity = userController.registerUser(userInputDTO, bindingResult);
        assertNotNull(userOutputDTOResponseEntity);
        assertTrue(userOutputDTOResponseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    void validateUser() {
        String token = UUID.randomUUID().toString();
        when(userServiceMock.validate(token)).thenReturn(userOutputDTO);
        ResponseEntity<UserOutputDTO> userOutputDTOResponseEntity = userController.validateToken(userInputDTO.withUpdatedToken(token));
        assertNotNull(userOutputDTOResponseEntity);
        assertTrue(userOutputDTOResponseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    void validateUserWhenUserServiceThrowException() {
        String token = UUID.randomUUID().toString();
        when(userServiceMock.validate(token)).thenThrow(UserException.class);
        ResponseEntity<UserOutputDTO> userOutputDTOResponseEntity = userController.validateToken(userInputDTO.withUpdatedToken(token));
        assertNotNull(userOutputDTOResponseEntity);
        assertTrue(userOutputDTOResponseEntity.getStatusCode().is4xxClientError());
    }

}