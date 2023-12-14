package com.wtf.cauth.service.impl;

import com.wtf.cauth.data.dto.request.user.UserAddReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.user.UserDto;
import com.wtf.cauth.data.model.User;
import com.wtf.cauth.data.model.UserCredential;
import com.wtf.cauth.exception.BadRequestException;
import com.wtf.cauth.exception.ResourceNotFoundException;
import com.wtf.cauth.mapper.UserModelMapper;
import com.wtf.cauth.repository.UserCredentialRepository;
import com.wtf.cauth.repository.UserRepository;
import com.wtf.cauth.service.AppService;
import com.wtf.cauth.service.UserService;
import com.wtf.cauth.util.BCryptUtil;
import com.wtf.cauth.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService  {
    private final AppService appService;
    private final UserModelMapper userModelMapper;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return getUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return getUser(user);
    }

    private User getUser(Optional<User> user) {
        if (user.isEmpty())
            throw new ResourceNotFoundException(false, Constants.USER_NOT_FOUND, null);
        return user.get();
    }

    @Override
    public List<UserDto> getUsers(String appName) {
        AppDto app = appService.getAppDtoByName(appName);
        List<User> users = userRepository.findByAppId(app.getId());
        Map<String, UserCredential> userCredentials = getUserCredentials(users);
        List<UserDto> res = new ArrayList<>();
        for (User user : users) {
            res.add(userModelMapper.convertUserToUserDto(user, app, userCredentials.get(user.getAppId())));
        }
        return res;
    }

    @Override
    public UserDto addUser(UserAddReqDto req) {
        AppDto app = appService.getAppDtoByName(req.getAppName());
        User user = userModelMapper.convertAddUserReqDtoToUser(req, app);
        List<User> existing = userRepository.findAllByAppIdAndEmailOrId(app.getId(), user.getEmail(), user.getId());
        if (!existing.isEmpty()) {
            throw new BadRequestException(false, "user with same email/id already exists", null);
        }
        user = userRepository.save(user);
        UserCredential userCredential = null;
        if (req.getPassword() != null) {
            String hashed = BCryptUtil.hash(req.getPassword());
            userCredential = UserCredential.newCredential(req.getId(), hashed);
            userCredentialRepository.save(userCredential);
        }
        return userModelMapper.convertUserToUserDto(user, app, userCredential);
    }

    private Map<String, UserCredential> getUserCredentials(List<User> users) {
        Map<String, UserCredential> res = new HashMap<>();
        for (User user : users) {
            res.put(user.getId(), null);
        }
        List<UserCredential> userCredentials = userCredentialRepository.findUserCredentialByUserIdInAndActive(res.keySet(), true);
        for (UserCredential userCredential : userCredentials) {
            res.put(userCredential.getUserId(), userCredential);
        }
        return res;
    }
}
