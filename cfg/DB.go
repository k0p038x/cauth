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
		return DBConfig{ConnURL: "postgres://krishna.preetham09:project=quiet-tree-321072;SaKdk9FjuI7y@ep-wandering-star-085518.us-east-2.aws.neon.tech/cauth?sslmode=require"}
	}
	return DBConfig{ConnURL: val}
}
