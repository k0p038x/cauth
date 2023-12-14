package com.wtf.cauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrMessage {
    private Boolean internal;
    private String code;
    private String message;
    private String detail;
}
