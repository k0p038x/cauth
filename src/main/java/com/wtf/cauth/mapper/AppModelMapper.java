package com.wtf.cauth.mapper;

import com.wtf.cauth.data.dto.request.app.OnboardAppReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.app.AppSensitiveDto;
import com.wtf.cauth.data.model.App;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppModelMapper {
    private final ModelMapper modelMapper;

    public App convertOnboardAppReqDtoToApp(OnboardAppReqDto req) {
        App app = modelMapper.map(req, App.class);
        app.setId(UUID.randomUUID().toString());
        app.setSecret(UUID.randomUUID().toString());
        return app;
    }

    public AppDto convertAppToAppDto(App app) {
        return modelMapper.map(app, AppDto.class);
    }

    public AppSensitiveDto convertAppToAppSensitiveDto(App app) {
        return modelMapper.map(app, AppSensitiveDto.class);
    }
}
