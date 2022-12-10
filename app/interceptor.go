package app

import (
	"fmt"
	"github.com/julienschmidt/httprouter"
	"math/rand"
	"net/http"
	"time"
)

func RandAlphabets(n int) string {
	const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}

func PerfInterceptor(handle httprouter.Handle) httprouter.Handle {
	return func(rw http.ResponseWriter, r *http.Request, p httprouter.Params) {
		tid := RandAlphabets(5)
		r.Header.Set("tid", tid)
		fmt.Printf("received request. tid: %s, ip: %s, type: %s, uri: %s\n", tid, r.RemoteAddr, r.Method, r.RequestURI)
		start := time.Now().UnixNano() / int64(time.Millisecond)
		handle(rw, r, p)
		end := time.Now().UnixNano() / int64(time.Millisecond)
		fmt.Printf("served request. tid: %s, response time: %d ms\n", tid, end-start)

	}
}
