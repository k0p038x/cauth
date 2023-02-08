package com.wtf.cauth.service;

import com.wtf.cauth.data.dto.request.user.AuthTokenReqDto;
import com.wtf.cauth.data.dto.request.user.UserLoginReqDto;
import com.wtf.cauth.data.dto.request.user.UserPasswordUpdateReqDto;
import com.wtf.cauth.data.dto.response.user.AuthTokenResDto;
import com.wtf.cauth.data.dto.response.user.UserLoginResDto;

public interface UserCredentialService {
    UserLoginResDto loginUser(UserLoginReqDto req);
    AuthTokenResDto verifyAuthToken(AuthTokenReqDto req);
    void updatePassword(UserPasswordUpdateReqDto req);
}
