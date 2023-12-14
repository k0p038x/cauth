package com.wtf.cauth.validator;

import com.wtf.cauth.data.dto.request.user.UserPasswordUpdateReqDto;
import com.wtf.cauth.data.model.UserCredential;
import com.wtf.cauth.exception.BadRequestException;
import com.wtf.cauth.exception.UnAuthenticatedException;
import com.wtf.cauth.util.BCryptUtil;
import com.wtf.cauth.util.Constants;

public class UserCredentialValidator {

    public static void validatePasswordUpdateReq(UserPasswordUpdateReqDto req, UserCredential activeCredential) {
        emptyPasswordCheck(req);
        passwordMatchCheck(req, activeCredential);
    }

    private static void passwordMatchCheck(UserPasswordUpdateReqDto req, UserCredential activeCredential) {
        if (!BCryptUtil.verify(req.getCurPassword(), activeCredential.getPassword()))
            throw new UnAuthenticatedException(false, Constants.WRONG_PASSWORD, null);
    }

    private static void emptyPasswordCheck(UserPasswordUpdateReqDto req) {
        if (req.getCurPassword() == null && !req.isReset())
            throw new BadRequestException(false, "password not found", null);
    }

}
