package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnAuthenticatedException extends BaseException {

    public UnAuthenticatedException(Boolean internal, String message, String detail) {
        super(internal, ErrorConstants.UN_AUTHENTICATED, message, detail);
    }
}
