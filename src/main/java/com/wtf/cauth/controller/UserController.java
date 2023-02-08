package com.wtf.cauth.controller;

import com.wtf.cauth.data.dto.request.user.UserAddReqDto;
import com.wtf.cauth.data.dto.request.user.AuthTokenReqDto;
import com.wtf.cauth.data.dto.request.user.UserLoginReqDto;
import com.wtf.cauth.data.dto.request.user.UserPasswordUpdateReqDto;
import com.wtf.cauth.data.dto.response.user.AuthTokenResDto;
import com.wtf.cauth.data.dto.response.user.UserDto;
import com.wtf.cauth.data.dto.response.user.UserLoginResDto;
import com.wtf.cauth.service.UserCredentialService;
import com.wtf.cauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/apis/v1")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final UserCredentialService userCredentialService;

    @GetMapping("/apps/{appName}/users")
    public List<UserDto> listUsers(@PathVariable String appName) {
        return userService.getUsers(appName);
    }

    @PostMapping("/apps/{appName}/users")
    public UserDto addUser(@RequestBody UserAddReqDto req, @PathVariable String appName) {
        return userService.addUser(req);
    }

    @PostMapping("/apps/{appName}/users/login")
    public UserLoginResDto loginUser(@RequestBody UserLoginReqDto req, @PathVariable String appName) {
        return userCredentialService.loginUser(req);
    }

    @PostMapping("/tokens/verify")
    public AuthTokenResDto verifyAuthToken(@RequestBody AuthTokenReqDto req) {
        return userCredentialService.verifyAuthToken(req);
    }

    @PutMapping("/apps/{appName}/users/password")
    public void resetPassword(@RequestBody UserPasswordUpdateReqDto req, @PathVariable String appName) {
         userCredentialService.updatePassword(req);
    }

}