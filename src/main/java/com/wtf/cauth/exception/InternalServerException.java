package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerException extends BaseException {
    public InternalServerException(String errorDescription) {
        super(ErrorConstants.INTERNAL_SERVER_ERROR, errorDescription);
    }
}
