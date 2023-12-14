package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerException extends BaseException {

    public InternalServerException(Boolean internal, String message, String detail) {
        super(internal, ErrorConstants.INTERNAL_SERVER_ERROR, message, detail);
    }
}
