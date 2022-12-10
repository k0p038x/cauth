package mapper

import (
	"cauth/internal/model"
	"cauth/pkg/dto"
	"golang.org/x/crypto/bcrypt"
	"time"
)

type IUserModelMapper interface {
	GetUserFromAddUserReqDto(appId string, dto dto.AddUserReqDto) model.User
	ConvertUsersToUserDtos(users []model.User) []dto.UserDto
	ConvertUserToUserDto(user model.User) dto.UserDto
}

type UserModelMapper struct {
}

func (mapper *UserModelMapper) GetUserFromAddUserReqDto(appId string, dto dto.AddUserReqDto) model.User {
	var passwordData = make([]model.PasswordData, 0)
	if dto.Password != "" {
		passwordInBytes := []byte(dto.Password)
		encryptedPassInBytes, _ := bcrypt.GenerateFromPassword(passwordInBytes, 0)
		entry := model.PasswordData{
			Value:     string(encryptedPassInBytes[:]),
			CreatedOn: time.Now().Unix(),
		}
		passwordData = append(passwordData, entry)
	}
	return model.User{
		Id:            dto.Id,
		Name:          dto.Name,
		Email:         dto.Email,
		EmailVerified: dto.EmailVerified,
		PasswordData:  passwordData,
		AppId:         appId,
	}
}

func (mapper *UserModelMapper) ConvertUserToUserDto(user model.User) dto.UserDto {
	return dto.UserDto{
		Id:                 user.Id,
		Name:               user.Name,
		Email:              user.Email,
		EmailVerified:      user.EmailVerified,
		PasswordConfigured: len(user.PasswordData) > 0,
	}
}

func (mapper *UserModelMapper) ConvertUsersToUserDtos(users []model.User) []dto.UserDto {
	var res = make([]dto.UserDto, 0)
	for _, user := range users {
		res = append(res, dto.UserDto{
			Id:                 user.Id,
			Name:               user.Name,
			Email:              user.Email,
			EmailVerified:      user.EmailVerified,
			PasswordConfigured: len(user.PasswordData) > 0,
		})
	}
	return res
}
