package com.wtf.cauth.service;

import com.wtf.cauth.data.dto.request.app.OnboardAppReqDto;
import com.wtf.cauth.data.dto.request.app.RefreshAppSecretReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.app.AppSensitiveDto;
import com.wtf.cauth.data.model.App;

import java.util.List;

public interface AppService {
    AppDto getAppDtoByName(String name);
    App getAppByName(String name);
    AppSensitiveDto onboardApp(OnboardAppReqDto req);
    List<AppDto> getApps();
    AppSensitiveDto refreshSecret(RefreshAppSecretReqDto req);
}
