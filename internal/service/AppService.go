package service

import (
	"cauth/internal/model"
	"cauth/internal/util"
	"cauth/internal/util/mapper"
	"cauth/pkg/dto"
	"fmt"
	"github.com/go-pg/pg/v10"
	"github.com/google/uuid"
)

type IAppService interface {
	OnboardApp(reqDto dto.OnboardAppReqDto) (*dto.OnboardAppResDto, *util.APIServiceError)
	GetApps() ([]dto.AppDto, *util.APIServiceError)
	RefreshSecret(reqDto dto.RefreshSecretReqDto) (*dto.RefreshSecretResDto, *util.APIServiceError)

	GetAppByName(name string) (model.App, error)
}

type AppService struct {
	DB     *pg.DB
	Mapper *mapper.IAppModelMapper
}

func (s *AppService) GetApps() ([]dto.AppDto, *util.APIServiceError) {
	var apps []model.App
	err := s.DB.Model(&apps).Select()
	if err != nil {
		fmt.Printf("unable to get apps. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	return (*s.Mapper).ConvertAppsToAppDtos(apps), nil
}

func (s *AppService) OnboardApp(req dto.OnboardAppReqDto) (*dto.OnboardAppResDto, *util.APIServiceError) {
	if err := req.Validate(); err != nil {
		return nil, err
	}
	existingApp, _ := s.GetAppByName(req.Name)
	if existingApp.Name == req.Name {
		return nil, util.BuildInvalidReqError("app name should be unique")
	}
	appId, appSecret := newUUID(), newUUID()
	app := (*s.Mapper).GetAppFromOnboardAppReqDto(req, appId, appSecret)
	_, err := s.DB.Model(&app).Insert()
	if err != nil {
		fmt.Printf("unable to insert entry in app table. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	return (*s.Mapper).ConvertAppToOnboardAppResDto(app), nil
}

func (s *AppService) RefreshSecret(req dto.RefreshSecretReqDto) (*dto.RefreshSecretResDto, *util.APIServiceError) {
	if err := req.Validate(); err != nil {
		return nil, err
	}
	app, err := s.GetAppByName(req.Name)
	if err != nil {
		fmt.Printf("unable to query. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	if app.Secret != req.CurSecret {
		return nil, util.BuildInvalidReqError("invalid secret")
	}
	secret := newUUID()
	app.Secret = secret
	_, err = s.DB.Model(&app).WherePK().Update()
	if err != nil {
		fmt.Printf("unable to change secret. error: %s\n", err.Error())
		return nil, util.BuildInternalServiceError(util.SOMETHING_WENT_WRONG)
	}
	return (*s.Mapper).ConvertAppToRefreshSecretResDto(app), nil
}

func (s *AppService) GetAppByName(name string) (model.App, error) {
	var app model.App
	filter := fmt.Sprintf("%s = ?", util.APP_NAME_COL)
	err := s.DB.Model(&app).Where(filter, name).Select()
	return app, err
}

func newUUID() string {
	uuid4 := uuid.New()
	return uuid4.String()
}
