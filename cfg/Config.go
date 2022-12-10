package cfg

import (
	"fmt"
	"os"
	"strconv"
)

const ADMIN_API_SECRET_KEY = "ADMIN_API_SECRET"
const DEFAULT_USER_AUTH_KEY_EXPIRY_KEY = "DEFAULT_USER_AUTH_KEY_EXPIRY"
const JWT_SECRET_KEY = "JWT_SECRET"
const DB_CONN_KEY = "CAUTH_DB_CONN"

type Config struct {
}

func GetConfigInstance() *Config {
	return &Config{}
}

func (config *Config) GetAdminApiSecret() string {
	val, ok := os.LookupEnv(ADMIN_API_SECRET_KEY)
	if !ok {
		fmt.Printf("%s env not set\n", ADMIN_API_SECRET_KEY)
	}
	return val
}

func (config *Config) GetDefaultUserAuthKeyExpiry() int32 {
	val, ok := os.LookupEnv(DEFAULT_USER_AUTH_KEY_EXPIRY_KEY)
	if !ok {
		fmt.Printf("%s env not set\n", DEFAULT_USER_AUTH_KEY_EXPIRY_KEY)
		return 24 * 60
	}
	valInInt, _ := strconv.Atoi(val)
	return int32(valInInt)
}

func (config *Config) GetJwtSecret() string {
	val, ok := os.LookupEnv(JWT_SECRET_KEY)
	if !ok {
		fmt.Printf("%s env not set\n", JWT_SECRET_KEY)
		return "sharedsecret"
	}
	return val
}
