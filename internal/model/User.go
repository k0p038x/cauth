package model

type PasswordData struct {
	Value     string
	CreatedOn int64
}

type User struct {
	Id                 string
	Name               string
	Email              string
	EmailVerified      bool
	PasswordData       []PasswordData
	AppId              string
}
