package mapper

import (
	"cauth/cfg"
	"cauth/internal/model"
	"cauth/pkg/dto"
)

type IAppModelMapper interface {
	GetAppFromOnboardAppReqDto(req dto.OnboardAppReqDto, appId string, appSecret string) model.App
	ConvertAppToOnboardAppResDto(app model.App) *dto.OnboardAppResDto
	ConvertAppToRefreshSecretResDto(app model.App) *dto.RefreshSecretResDto
	ConvertAppsToAppDtos(apps []model.App) []dto.AppDto
}

type AppModelMapper struct {
	config *cfg.Config
}

func (mapper *AppModelMapper) GetAppFromOnboardAppReqDto(req dto.OnboardAppReqDto, id string, secret string) model.App {
	expiry := req.UserAuthKeyExpiryInMins
	if expiry == 0 {
		expiry = mapper.config.GetDefaultUserAuthKeyExpiry()
	}
	return model.App{
		Id:                      id,
		Name:                    req.Name,
		OwnerEmail:              req.OwnerEmail,
		Secret:                  secret,
		UserAuthKeyExpiryInMins: expiry,
	}
}

func (mapper *AppModelMapper) ConvertAppToOnboardAppResDto(app model.App) *dto.OnboardAppResDto {
	return &dto.OnboardAppResDto{
		Name:                    app.Name,
		OwnerEmail:              app.OwnerEmail,
		Id:                      app.Id,
		Secret:                  app.Secret,
		UserAuthKeyExpiryInMins: app.UserAuthKeyExpiryInMins,
	}
}

func (mapper *AppModelMapper) ConvertAppToRefreshSecretResDto(app model.App) *dto.RefreshSecretResDto {
	return &dto.RefreshSecretResDto{
		Name:   app.Name,
		Secret: app.Secret,
	}
}

func (mapper *AppModelMapper) ConvertAppsToAppDtos(apps []model.App) []dto.AppDto {
	var res = make([]dto.AppDto, 0)
	for _, app := range apps {
		res = append(res, dto.AppDto{
			Name:                    app.Name,
			Id:                      app.Id,
			OwnerEmail:              app.OwnerEmail,
			UserAuthKeyExpiryInMins: app.UserAuthKeyExpiryInMins,
		})
	}
	return res
}
