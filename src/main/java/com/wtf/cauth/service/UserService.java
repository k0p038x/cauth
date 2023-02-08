package com.wtf.cauth.service;

import com.wtf.cauth.data.dto.request.user.UserAddReqDto;
import com.wtf.cauth.data.dto.request.user.AuthTokenReqDto;
import com.wtf.cauth.data.dto.request.user.UserLoginReqDto;
import com.wtf.cauth.data.dto.response.user.AuthTokenResDto;
import com.wtf.cauth.data.dto.response.user.UserDto;
import com.wtf.cauth.data.dto.response.user.UserLoginResDto;
import com.wtf.cauth.data.model.User;

import java.util.List;

public interface UserService {
    User getUser(String id);
    List<UserDto> getUsers(String appName);
    UserDto addUser(UserAddReqDto req);
}
