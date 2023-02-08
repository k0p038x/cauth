package com.wtf.cauth.service.impl;

import com.wtf.cauth.data.dto.request.app.OnboardAppReqDto;
import com.wtf.cauth.data.dto.request.app.RefreshAppSecretReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.app.AppSensitiveDto;
import com.wtf.cauth.data.model.App;
import com.wtf.cauth.exception.BadRequestException;
import com.wtf.cauth.exception.ResourceNotFoundException;
import com.wtf.cauth.mapper.AppModelMapper;
import com.wtf.cauth.repository.AppRepository;
import com.wtf.cauth.service.AppService;
import com.wtf.cauth.util.BCryptUtil;
import com.wtf.cauth.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppServiceImpl implements AppService  {
    private final AppRepository appRepository;
    private final AppModelMapper appModelMapper;

    @Override
    public AppDto getAppDtoByName(String name) {
        App app = getAppByName(name);
        return appModelMapper.convertAppToAppDto(app);
    }

    @Override
    public App getAppByName(String name) {
        Optional<App> app = appRepository.findByName(name);
        if (app.isEmpty())
            throw new ResourceNotFoundException(Constants.APP_NOT_FOUND);
        return app.get();
    }

    @Override
    public AppSensitiveDto onboardApp(OnboardAppReqDto req) {
        if (appRepository.findByName(req.getName()).isPresent())
            throw new ResourceNotFoundException(Constants.APP_ALREADY_EXISTS);
        App app = appModelMapper.convertOnboardAppReqDtoToApp(req);
        AppSensitiveDto appSensitiveDto = appModelMapper.convertAppToAppSensitiveDto(app);
        hashAppSecretAndSave(app);
        return appSensitiveDto;
    }

    @Override
    public List<AppDto> getApps() {
        List<App> apps = appRepository.findAll();
        List<AppDto> res = new ArrayList<>();
        for (App app : apps) {
            res.add(appModelMapper.convertAppToAppDto(app));
        }
        return res;
    }

    @Override
    public AppSensitiveDto refreshSecret(RefreshAppSecretReqDto req) {
        App app = getAppByName(req.getName());
        if (!BCryptUtil.verify(req.getCurSecret(), app.getSecret()))
            throw new BadRequestException(Constants.INVALID_APP_SECRET);
        app.setSecret(UUID.randomUUID().toString());
        AppSensitiveDto appSensitiveDto = appModelMapper.convertAppToAppSensitiveDto(app);
        hashAppSecretAndSave(app);
        return appSensitiveDto;
    }

    private void hashAppSecretAndSave(App app) {
        String hashed = BCryptUtil.hash(app.getSecret());
        app.setSecret(hashed);
        appRepository.save(app);
    }
}
