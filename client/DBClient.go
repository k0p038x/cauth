package client

import (
	"cauth/cfg"
	"github.com/go-pg/pg/v10"
)

func GetDBInstance() *pg.DB {
	config := cfg.GetDBConfig()
	opt, err := pg.ParseURL(config.ConnURL)
	if err != nil {
		panic(err)
	}
	db := pg.Connect(opt)
	return db
}
