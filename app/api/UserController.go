package api

import (
	"cauth/app"
	"cauth/cfg"
	"cauth/internal/service"
	"cauth/internal/util"
	"cauth/pkg/dto"
	"encoding/json"
	"fmt"
	"github.com/julienschmidt/httprouter"
	"net/http"
)

type IUserController interface {
	AddUser(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	GetUsers(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	LoginUser(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	VerifyAuthToken(rw http.ResponseWriter, r *http.Request, p httprouter.Params)
	ConfigureRoutes(r *httprouter.Router)
	VerifyAppSecret(h httprouter.Handle) httprouter.Handle
}

type UserController struct {
	UserService *service.IUserService
	AppService  *service.IAppService
	Config      *cfg.Config
}

func (controller *UserController) ConfigureRoutes(router *httprouter.Router) {
	verifyAppSecret := controller.VerifyAppSecret
	perf := app.PerfInterceptor
	router.POST("/apps/:app/users", perf(verifyAppSecret(controller.AddUser)))
	router.GET("/apps/:app/users", perf(verifyAppSecret(controller.GetUsers)))
	router.POST("/apps/:app/users/:user/login", perf(verifyAppSecret(controller.LoginUser)))
	router.POST("/apps/:app/users/:user/token/verify", perf(verifyAppSecret(controller.VerifyAuthToken)))
}

func (controller *UserController) VerifyAppSecret(handle httprouter.Handle) httprouter.Handle {
	return func(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
		err := controller.verifyAppSecret(r, p)
		if err != nil {
			util.SendJSONResponse(rw, err, err.Code)
		} else {
			fmt.Println("successfully verified app secret")
			handle(rw, r, p)
		}
	}
}

func (controller *UserController) verifyAppSecret(r *http.Request, p httprouter.Params) *util.APIServiceError {
	app, _ := (*controller.AppService).GetAppByName(p.ByName("app"))
	if app.Name == "" {
		return util.BuildInvalidReqError("app not found")
	}
	if app.Secret != r.Header.Get(util.APP_SECRET_KEY_HEADER) {
		return util.BuildUnAuthorizedError(util.INVALID_APP_SECRET)
	}
	return nil
}

func (controller *UserController) AddUser(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	var req dto.AddUserReqDto
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	res, err := (*controller.UserService).AddUser(req)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusCreated)
}

func (controller *UserController) GetUsers(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	appName := p.ByName("app")
	res, err := (*controller.UserService).GetUsers(appName)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusOK)
}

func (controller *UserController) LoginUser(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	var req dto.UserLoginReqDto
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	res, err := (*controller.UserService).LoginUser(req)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusOK)
}

func (controller *UserController) VerifyAuthToken(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
	var req dto.VerifyAuthTokenReqDto
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	res, err := (*controller.UserService).VerifyAuthToken(req)
	if err != nil {
		util.SendJSONResponse(rw, err, err.Code)
		return
	}
	util.SendJSONResponse(rw, res, http.StatusOK)
}
