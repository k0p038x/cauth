package util

import (
	"net/http"
)

const SOMETHING_WENT_WRONG string = "Fck, Something went wrong"
const INVALID_TOKEN_ERROR string = "invalid token"
const INVALID_APP_SECRET string = "invalid app secret"
const INVALID_ADMIN_SECRET string = "invalid admin secret"

type APIServiceError struct {
	Description string `json:"error_description"`
	Code        int    `json:"error_code"`
}

func buildServiceError(desc string, code int) *APIServiceError {
	return &APIServiceError{
		Description: desc,
		Code:        code,
	}
}

func BuildUnAuthorizedError(desc string) *APIServiceError {
	return buildServiceError(desc, http.StatusUnauthorized)
}

func BuildInternalServiceError(desc string) *APIServiceError {
	return buildServiceError(desc, http.StatusInternalServerError)
}

func BuildInvalidReqError(desc string) *APIServiceError {
	return buildServiceError(desc, http.StatusBadRequest)
}
