package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseException extends RuntimeException {
    private Boolean internal;
    private String code;
    private String message;
    private String detail;

    public BaseException(Boolean internal, String code, String message, String detail) {
        super(code);
        this.internal = internal;
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}
