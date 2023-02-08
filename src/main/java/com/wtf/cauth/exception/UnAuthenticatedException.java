package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

import static com.wtf.cauth.exception.ErrorConstants.UN_AUTHENTICATED;

@Getter
@Setter
public class UnAuthenticatedException extends BaseException {

    public UnAuthenticatedException(String errorDescription) {
        super(UN_AUTHENTICATED, errorDescription);
    }
}
