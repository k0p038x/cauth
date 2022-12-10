package model

type App struct {
	Id                      string
	Name                    string
	OwnerEmail              string
	Secret                  string
	UserAuthKeyExpiryInMins int32
}
