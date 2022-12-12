package cfg

import (
	"fmt"
	"os"
)

type DBConfig struct {
	ConnURL string
}

func GetDBConfig() DBConfig {
	val, ok := os.LookupEnv(DB_CONN_KEY)
	if !ok {
		fmt.Printf("%s env not set\n", DB_CONN_KEY)
	}
	return DBConfig{ConnURL: val}
}
