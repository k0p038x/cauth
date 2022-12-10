package service

import (
	"cauth/cfg"
	"cauth/internal/model"
	"cauth/internal/util"
	"cauth/internal/util/mapper"
	"cauth/pkg/dto"
	"errors"
	"fmt"
	"github.com/go-pg/pg/v10"
	"github.com/golang-jwt/jwt"
	"golang.org/x/crypto/bcrypt"
	"time"
)

type IUserService interface {
	AddUser(req dto.AddUserReqDto) (*dto.UserDto, *util.APIServiceError)
	GetUsers(appName string) ([]dto.UserDto, *util.APIServiceError)
	LoginUser(req dto.UserLoginReqDto) (*dto.UserLoginResDto, *util.APIServiceError)
	VerifyAuthToken(req dto.VerifyAuthTokenReqDto) (*dto.VerifyAuthTokenResDto, *util.APIServiceError)

	GenerateAuthToken(user model.User, app model.App) (string, int64)
}

type UserService struct {
	AppService *IAppService
	DB         *pg.DB
	Mapper     *mapper.IUserModelMapper
	Config     *cfg.Config
}

func (s *UserService) VerifyAuthToken(req dto.VerifyAuthTokenReqDto) (*dto.VerifyAuthTokenResDto, *util.APIServiceError) {
	token, err := jwt.Parse(req.AccessToken, func(token *jwt.Token) (interface{}, error) {
		_, ok := token.Method.(*jwt.SigningMethodHMAC)
		if !ok {
			return nil, errors.New("unauthorized")
		}
		return []byte(s.Config.GetJwtSecret()), nil
	})
	if err != nil {
		return nil, util.BuildInvalidReqError(util.INVALID_TOKEN_ERROR)
	}

	claims, _ := token.Claims.(jwt.MapClaims)
	return &dto.VerifyAuthTokenResDto{
		ExpiresOn: int64(claims["exp"].(float64)),
		UserId:    claims["id"].(string),
		AppName:   claims["app"].(string),
	}, nil
}

func (s *UserService) GenerateAuthToken(user model.User, app model.App) (string, int64) {
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	expTimestamp := time.Now().Add(time.Duration(app.UserAuthKeyExpiryInMins) * time.Minute).Unix()
	claims["exp"] = expTimestamp
	claims["iss"] = "cauth"
	claims["id"] = user.Id
	claims["app"] = app.Name
	signedToken, err := token.SignedString([]byte(s.Config.GetJwtSecret()))
	if err != nil {
		fmt.Printf("error in signing secret. error: %s\n", err.Error())
		return "error", 0
	}
	return signedToken, expTimestamp
}

func (s *UserService) LoginUser(req dto.UserLoginReqDto) (*dto.UserLoginResDto, *util.APIServiceError) {
	app, user, err := s.GetUserById(req.AppName, req.Id)
	if err != nil {
		return nil, err
	}
	if user.Id == "" {
		return nil, util.BuildInvalidReqError("user not found")
	}
	enteredPwd := []byte(req.Password)
	passwordData := user.PasswordData[len(user.PasswordData)-1]
	hashedPassword := []byte(passwordData.Value)
	status := bcrypt.CompareHashAndPassword(hashedPassword, enteredPwd)
	token, expiry := s.GenerateAuthToken(*user, *app)
	if status == nil {
		return &dto.UserLoginResDto{
			AccessToken: token,
			ExpiresOn:   expiry,
			UserId:      user.Id,
			AppName:     req.AppName,
		}, nil
	} else {
		return nil, util.BuildUnAuthorizedError("invalid password")
	}
}

func (s *UserService) GetUsers(appName string) ([]dto.UserDto, *util.APIServiceError) {
	var appId string
	if app, err := s.getAppByName(appName); err != nil {
		return nil, err
	} else {
		appId = app.Id
	}

	var users []model.User
	filter := fmt.Sprintf("%s = ?", util.USER_APP_ID)
	err := s.DB.Model(&users).Where(filter, appId).Select()
	if err != nil {
		fmt.Printf("unable to get users. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	return (*s.Mapper).ConvertUsersToUserDtos(users), nil
}

func (s *UserService) AddUser(req dto.AddUserReqDto) (*dto.UserDto, *util.APIServiceError) {
	if err := req.Validate(); err != nil {
		return nil, util.BuildInvalidReqError(err.Error())
	}

	var appId string
	if app, err := s.getAppByName(req.AppName); err != nil {
		return nil, err
	} else {
		appId = app.Id
	}

	user := (*s.Mapper).GetUserFromAddUserReqDto(appId, req)
	_, err := s.DB.Model(&user).Insert()
	if err != nil {
		fmt.Printf("unable to insert entry in users table. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	res := (*s.Mapper).ConvertUserToUserDto(user)
	return &res, nil
}

func (s *UserService) getAppByName(appName string) (*model.App, *util.APIServiceError) {
	app, _ := (*s.AppService).GetAppByName(appName)
	if app.Name == "" {
		return nil, util.BuildInvalidReqError("app not found")
	}
	return &app, nil
}

func (s *UserService) GetUserById(appName string, userId string) (*model.App, *model.User, *util.APIServiceError) {
	app, err := s.getAppByName(appName)
	if err != nil {
		return nil, nil, err
	}

	var user model.User
	filterByApp := fmt.Sprintf("%s = ?", util.USER_APP_ID)
	filterByUser := fmt.Sprintf("%s = ?", util.USER_ID_COL)
	dbErr := s.DB.Model(&user).Where(filterByApp, app.Id).Where(filterByUser, userId).Select()
	if dbErr != nil {
		fmt.Printf("unable to get user details. error: %s\n", dbErr.Error())
		return nil, nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	return app, &user, nil
}
