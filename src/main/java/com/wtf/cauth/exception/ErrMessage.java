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
    private String errorCode;
    private String errorDescription;
}
