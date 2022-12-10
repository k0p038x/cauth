package main

import (
	"cauth/app/api"
	"cauth/cfg"
	"cauth/client"
	"cauth/internal/service"
	"cauth/internal/util/mapper"
	"fmt"
	"github.com/julienschmidt/httprouter"
	"net/http"
)

func main() {
	router := httprouter.New()
	db := client.GetDBInstance()
	config := cfg.GetConfigInstance()

	// entity: app
	// service, mapper, controller
	var appModelMapper mapper.IAppModelMapper = &mapper.AppModelMapper{}
	var appService service.IAppService = &service.AppService{
		DB:     db,
		Mapper: &appModelMapper,
	}
	var appController api.IAppController = &api.AppController{
		AppService: &appService,
		Config:     config,
	}
	appController.ConfigureRoutes(router)

	// entity: user
	// service, mapper, controller
	var userModelMaper mapper.IUserModelMapper = &mapper.UserModelMapper{}
	var userService service.IUserService = &service.UserService{
		AppService: &appService,
		DB:         db,
		Mapper:     &userModelMaper,
		Config:     config,
	}
	var userController api.IUserController = &api.UserController{
		UserService: &userService,
		AppService:  &appService,
		Config:      config,
	}
	userController.ConfigureRoutes(router)

	fmt.Println("Starting server on :8080")
	http.ListenAndServe(":8080", router)
}
