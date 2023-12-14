package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends BaseException {

    public BadRequestException(Boolean internal, String message, String detail) {
        super(internal, ErrorConstants.BAD_REQUEST, message, detail);
    }
}
