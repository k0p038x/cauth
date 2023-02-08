package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private String errorCode;
    private String errorDescription;

    public BaseException(String errorCode, String errorDescription) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
