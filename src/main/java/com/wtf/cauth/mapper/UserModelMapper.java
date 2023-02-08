package com.wtf.cauth.mapper;

import com.wtf.cauth.data.dto.request.user.UserAddReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.user.UserDto;
import com.wtf.cauth.data.model.User;
import com.wtf.cauth.data.model.UserCredential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserModelMapper {
    private final ModelMapper mapper;

    public User convertAddUserReqDtoToUser(UserAddReqDto req, AppDto appDto) {
        User user = mapper.map(req, User.class);
        user.setAppId(appDto.getId());
        return user;
    }

    public UserDto convertUserToUserDto(User user, AppDto appDto, UserCredential userCredential) {
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setAppName(appDto.getName());
        userDto.setPasswordConfigured(Objects.nonNull(userCredential));
        return userDto;
    }
}
