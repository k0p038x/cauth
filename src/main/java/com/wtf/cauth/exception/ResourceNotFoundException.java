package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(Boolean internal, String message, String detail) {
        super(internal, ErrorConstants.NOT_FOUND, message, detail);
    }
}
