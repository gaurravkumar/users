package com.prototype.users.service;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.entity.UserEntity;
import com.prototype.users.exception.UserException;
import com.prototype.users.mapper.UserMapper;
import com.prototype.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserOutputDTO register(final UserInputDTO userInput) throws UserException {

        // Check if the username already exist
        Optional<UserEntity> queriedUser = userRepository.findByEmail(userInput.email());

        if (!queriedUser.isPresent()) {
            //Generate a unique token
            UserInputDTO decoratedDTOWithToken = userInput.withUpdatedToken(UUID.randomUUID().toString());

            UserEntity userEntity = userMapper.userDTOToUserEntity(decoratedDTOWithToken);
            var databaseRecord = userRepository.save(userEntity);
            UserOutputDTO userOutputDTO = userMapper.userEntityToUserOutputDTO(databaseRecord);

            return userOutputDTO;
        }
        throw new UserException(userInput.name() + " already exist");
    }

    @Override
    public UserOutputDTO validate(final String token) throws UserException {

        var databaseRecord = userRepository.findByToken(UUID.fromString(token));
        if (databaseRecord.isPresent()) {
            return userMapper.userEntityToUserOutputDTO(databaseRecord.get());
        }
        throw new UserException("User not found");
    }

    @Override
    public void deRegister(String token) {

        Optional<UserEntity> queriedUser = userRepository.findByToken(UUID.fromString(token));

        if (queriedUser.isPresent()) {
            userRepository.delete(queriedUser.get());
            return;
        }
        throw new UserException("User does not exist");
    }
}
