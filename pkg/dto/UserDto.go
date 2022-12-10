package dto

import "errors"

type AddUserReqDto struct {
	Id            string `json:"id"`
	Name          string `json:"name"`
	Email         string `json:"email"`
	EmailVerified bool   `json:"email_verified"`
	Password      string `json:"password"`
	AppName       string `json:"app_name"`
}

func (dto *AddUserReqDto) Validate() error {
	if dto.Name == "" || dto.AppName == "" {
		return errors.New("user name and app name cant be empty")
	}
	return nil
}

type UserDto struct {
	Id                 string `json:"id"`
	Name               string `json:"name"`
	Email              string `json:"email"`
	EmailVerified      bool   `json:"email_verified"`
	PasswordConfigured bool   `json:"password_configured"`
}

type UserLoginReqDto struct {
	Id       string `json:"id"`
	Password string `json:"password"`
	AppName  string `json:"app_name"`
}

type UserLoginResDto struct {
	AccessToken string `json:"access_token"`
	ExpiresOn   int64  `json:"expires_on"`
	UserId      string `json:"user_id"`
	AppName     string `json:"app_name"`
}

type VerifyAuthTokenReqDto struct {
	AccessToken string `json:"access_token"`
}

type VerifyAuthTokenResDto struct {
	ExpiresOn int64  `json:"expires_on"`
	UserId    string `json:"user_id"`
	AppName   string `json:"app_name"`
}
