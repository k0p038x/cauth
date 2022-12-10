package dto

import (
	"cauth/internal/util"
)

type OnboardAppReqDto struct {
	Name                    string `json:"name"`
	OwnerEmail              string `json:"owner_email"`
	UserAuthKeyExpiryInMins int32  `json:"user_auth_key_expiry_in_mins"`
}

func (req OnboardAppReqDto) Validate() *util.APIServiceError {
	if req.Name == "" {
		return util.BuildInvalidReqError("app name can't not be null")
	}
	if req.OwnerEmail == "" {
		return util.BuildInvalidReqError("app owner email must present")
	}
	return nil
}

type OnboardAppResDto struct {
	Name                    string `json:"name"`
	OwnerEmail              string `json:"owner_email"`
	Id                      string `json:"id"`
	Secret                  string `json:"secret"`
	UserAuthKeyExpiryInMins int32  `json:"user_auth_key_expiry_in_mins"`
}

type RefreshSecretReqDto struct {
	Name      string `json:"name"`
	CurSecret string `json:"cur_secret"`
}

func (dto RefreshSecretReqDto) Validate() *util.APIServiceError {
	if dto.Name == "" || dto.CurSecret == "" {
		return util.BuildInvalidReqError("app name and current secret should not be empty")
	}
	return nil
}

type RefreshSecretResDto struct {
	Name   string `json:"name"`
	Secret string `json:"secret"`
}

type AppDto struct {
	Name                    string `json:"name"`
	Id                      string `json:"id"`
	OwnerEmail              string `json:"owner_email"`
	UserAuthKeyExpiryInMins int32  `json:"user_auth_key_expiry_in_mins"`
}
