package com.wtf.cauth.service.impl;

import com.wtf.cauth.data.dto.request.user.AuthTokenReqDto;
import com.wtf.cauth.data.dto.request.user.UserLoginReqDto;
import com.wtf.cauth.data.dto.request.user.UserPasswordUpdateReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.user.AuthTokenResDto;
import com.wtf.cauth.data.dto.response.user.UserLoginResDto;
import com.wtf.cauth.data.model.User;
import com.wtf.cauth.data.model.UserCredential;
import com.wtf.cauth.exception.BadRequestException;
import com.wtf.cauth.exception.UnAuthenticatedException;
import com.wtf.cauth.repository.UserCredentialRepository;
import com.wtf.cauth.service.AppService;
import com.wtf.cauth.service.UserCredentialService;
import com.wtf.cauth.service.UserService;
import com.wtf.cauth.util.BCryptUtil;
import com.wtf.cauth.util.Constants;
import com.wtf.cauth.util.JwtUtil;
import com.wtf.cauth.validator.UserCredentialValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserCredentialServiceImpl implements UserCredentialService {
    private final AppService appService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public UserLoginResDto loginUser(UserLoginReqDto req) {
        AppDto app = appService.getAppDtoByName(req.getAppName());
        User user;
        if (req.getId() != null)
            user = userService.getUserById(req.getId());
        else if (req.getEmail() != null)
            user = userService.getUserByEmail(req.getEmail());
        else
            throw new BadRequestException("id or email required");
        UserCredential activeCredential = getActiveUserCredential(user.getId());
        if (!BCryptUtil.verify(req.getPassword(), activeCredential.getPassword()))
            throw new UnAuthenticatedException(Constants.WRONG_PASSWORD);
        Pair<String, Long> tokenData = jwtUtil.generateAuthToken(app, user);
        String jwt = tokenData.getFirst();
        long expiresOn = tokenData.getSecond();
        return new UserLoginResDto(jwt, expiresOn, user.getId(), user.getRole(), app.getName());
    }

    @Override
    public AuthTokenResDto verifyAuthToken(AuthTokenReqDto req) {
        AuthTokenResDto res = jwtUtil.validateAuthToken(req.getAuthToken());
        if (!res.isValid())
            throw new UnAuthenticatedException(Constants.INVALID_OR_EXPIRED_TOKEN);
        return res;
    }

    @Override
    public void updatePassword(UserPasswordUpdateReqDto req) {
        UserCredential existingCredential = getActiveUserCredential(req.getId());
        UserCredentialValidator.validatePasswordUpdateReq(req, existingCredential);
        UserCredential newCredential = UserCredential.newCredential(req.getId(), req.getNewPassword());
        existingCredential.setActive(false);
        userCredentialRepository.saveAll(Arrays.asList(existingCredential, newCredential));
    }

    private UserCredential getActiveUserCredential(String id) {
        List<UserCredential> userCredentials = userCredentialRepository.findUserCredentialByUserIdAndActive(id, true);
        if (userCredentials.isEmpty())
            throw new BadRequestException(Constants.PASSWORD_NOT_SET);
        return userCredentials.get(0);
    }

}
