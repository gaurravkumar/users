package com.prototype.users.mapper;

import com.prototype.users.dto.UserInputDTO;
import com.prototype.users.dto.UserOutputDTO;
import com.prototype.users.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity userDTOToUserEntity(UserInputDTO userInput);

    UserOutputDTO userEntityToUserOutputDTO(UserEntity userEntity);
}
