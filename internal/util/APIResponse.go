package util

import (
	"encoding/json"
	"net/http"
)

func SendJSONResponse(rw http.ResponseWriter, res interface{}, code int) {
	payload, err := json.Marshal(res)
	if err != nil {
		SendJSONResponse(rw, APIServiceError{
			Description: "JSON encoding error",
			Code:        http.StatusInternalServerError,
		}, http.StatusInternalServerError)
	} else {
		rw.Header().Set("Content-Type", "application/json")
		rw.WriteHeader(code)
		rw.Write(payload)
	}
}
