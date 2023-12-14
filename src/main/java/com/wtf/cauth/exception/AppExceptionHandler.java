package com.wtf.cauth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrMessage handleBadRequestException(BadRequestException e) {
        return getErrMessage(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        return getErrMessage(e);
    }

    private ErrMessage getErrMessage(BaseException e) {
        log.error("sending error response. exception: {}", e.toString());
        return new ErrMessage(e.getInternal(), e.getCode(), e.getMessage(), e.getDetail());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrMessage handleRuntimeException(RuntimeException e) {
        log.error("unhandled exception. exception: {}", e.getCause().toString());
        return new ErrMessage(true, "server_error", "cauth server error", e.getCause().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrMessage handleInternalServerException(InternalServerException e) {
        return getErrMessage(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrMessage handleInvalidCredentials(UnAuthenticatedException e) {
        return getErrMessage(e);
    }

}
