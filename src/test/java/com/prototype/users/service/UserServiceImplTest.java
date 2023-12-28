package com.prototype.users.service;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.entity.UserEntity;
import com.prototype.users.exception.UserException;
import com.prototype.users.mapper.UserMapper;
import com.prototype.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserMapper userMapper;
    @Captor
    ArgumentCaptor<UserInputDTO> userInputDTOArgumentCaptor;
    @Captor
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor;
    @InjectMocks
    private UserServiceImpl userService;

    UserEntity userEntity;
    UserInputDTO userInputDTO;

    @BeforeEach
    void setUpData() {
        userEntity = new UserEntity();
        userEntity.setEmail("email");
        userEntity.setName("name");
        userEntity.setPassword("dfsdfds-eree-sdfsdfd-wewefs");
        userEntity.setToken(UUID.randomUUID());

        userInputDTO = new UserInputDTO("name", "email", "password", null);
    }

    @Test
    void registerSuccessfully() {
        when(mockUserRepository.findByEmail(userInputDTO.email())).thenReturn(Optional.empty());
        when(userMapper.userDTOToUserEntity(any())).thenReturn(userEntity);
        when(mockUserRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.userEntityToUserOutputDTO(userEntity)).thenReturn(new UserOutputDTO("name", "", LocalDateTime.now(), null));
        assertNotNull(userService.register(userInputDTO));
        verify(userMapper).userDTOToUserEntity(userInputDTOArgumentCaptor.capture());
        UserInputDTO inputDTO = userInputDTOArgumentCaptor.getValue();
        assertEquals("name", inputDTO.name());
        assertEquals("email", inputDTO.email());
        assertNotNull(inputDTO.password());
        assertNotEquals("password", inputDTO.password());
        assertNotNull(inputDTO.token());
    }

    @Test
    void registerUserExistThrowsException() {
        when(mockUserRepository.findByEmail(userInputDTO.email())).thenReturn(Optional.of(userEntity));
        assertThrows(UserException.class, () -> userService.register(userInputDTO), "name already exist");
    }

    @Test
    void validateUserExist() {
        when(mockUserRepository.findByToken(any())).thenReturn(Optional.of(userEntity));
        when(userMapper.userEntityToUserOutputDTO(userEntity)).thenReturn(new UserOutputDTO("name", "", LocalDateTime.now(), null));
        assertNotNull(userService.validate(UUID.randomUUID().toString()));
    }

    @Test
    void validateUserDoesNotExist() {
        when(mockUserRepository.findByToken(any())).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> userService.validate(UUID.randomUUID().toString()), "User not found");
    }

    @Test
    void deRegister() {
        UUID token = UUID.randomUUID();
        userEntity.setToken(UUID.fromString("6a3e9f0c-2c58-4eb9-bfda-114c5d0c6a37"));
        when(mockUserRepository.findByToken(any())).thenReturn(Optional.of(userEntity));
        userService.deRegister(token.toString());
        verify(mockUserRepository).delete(userEntityArgumentCaptor.capture());
        var user = userEntityArgumentCaptor.getValue();
        assertEquals(UUID.fromString("6a3e9f0c-2c58-4eb9-bfda-114c5d0c6a37"), user.getToken());
    }

    @Test
    void deRegisterUserNotPresent() {
        {
            when(mockUserRepository.findByToken(any())).thenReturn(Optional.empty());
            assertThrows(UserException.class, () -> userService.deRegister(UUID.randomUUID().toString()), "User does not exist");
        }
    }
}