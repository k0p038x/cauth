package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends BaseException {

    public BadRequestException(String errorDescription) {
        super(ErrorConstants.BAD_REQUEST, errorDescription);
    }
}
