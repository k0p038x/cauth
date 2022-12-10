package api

import (
	"cauth/app"
	"cauth/cfg"
	"cauth/internal/service"
	"cauth/internal/util"
	"cauth/pkg/dto"
	"encoding/json"
	"github.com/julienschmidt/httprouter"
	"net/http"
)

type IAppController interface {
	OnboardApp(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	RefreshSecret(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	GetApps(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	VerifyAdminSecret(handle httprouter.Handle) httprouter.Handle
	ConfigureRoutes(r *httprouter.Router)
}

type AppController struct {
	AppService *service.IAppService
	Config     *cfg.Config
}

func (controller *AppController) ConfigureRoutes(router *httprouter.Router) {
	verifyAdminSecret := controller.VerifyAdminSecret
	perf := app.PerfInterceptor
	router.POST("/apps", perf(verifyAdminSecret(controller.OnboardApp)))
	router.GET("/apps", perf(controller.GetApps))
	router.POST("/apps/:app/secret/refresh", perf(verifyAdminSecret(controller.RefreshSecret)))
}

func (controller *AppController) VerifyAdminSecret(handle httprouter.Handle) httprouter.Handle {
	return func(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
		err := controller.verifyAdminSecret(r)
		if err == nil {
			handle(rw, r, p)
		} else {
			util.SendJSONResponse(rw, err, err.Code)
		}
	}
}

func (controller *AppController) verifyAdminSecret(r *http.Request) *util.APIServiceError {
	config := *controller.Config
	headerSecret := r.Header.Get(util.ADMIN_SECRET_KEY_HEADER)
	if config.GetAdminApiSecret() != headerSecret {
		return util.BuildUnAuthorizedError("invalid admin secret")
	}
	return nil
}

func (controller *AppController) OnboardApp(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	if authError := controller.verifyAdminSecret(r); authError != nil {
		util.SendJSONResponse(rw, authError, authError.Code)
		return
	}

	var req dto.OnboardAppReqDto
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	res, err := (*controller.AppService).OnboardApp(req)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusCreated)
}

func (controller *AppController) RefreshSecret(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	if authError := controller.verifyAdminSecret(r); authError != nil {
		util.SendJSONResponse(rw, authError, authError.Code)
		return
	}

	var req dto.RefreshSecretReqDto
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	res, err := (*controller.AppService).RefreshSecret(req)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusCreated)
}

func (controller *AppController) GetApps(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	if authError := controller.verifyAdminSecret(r); authError != nil {
		util.SendJSONResponse(rw, authError, authError.Code)
		return
	}

	res, err := (*controller.AppService).GetApps()
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusOK)
}
