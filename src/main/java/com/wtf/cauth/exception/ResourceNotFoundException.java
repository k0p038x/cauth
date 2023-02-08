package com.wtf.cauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String errorDescription) {
        super(ErrorConstants.NOT_FOUND, errorDescription);
    }
}
