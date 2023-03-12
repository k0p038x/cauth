package com.wtf.cauth.controller;

import com.wtf.cauth.context.RequireAdminSecret;
import com.wtf.cauth.context.RequireAppSecret;
import com.wtf.cauth.data.dto.request.app.OnboardAppReqDto;
import com.wtf.cauth.data.dto.request.app.RefreshAppSecretReqDto;
import com.wtf.cauth.data.dto.response.app.AppDto;
import com.wtf.cauth.data.dto.response.app.AppSensitiveDto;
import com.wtf.cauth.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/apis/v1/apps")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppController {
    private final AppService appService;

    @GetMapping()
    @RequireAdminSecret
    public List<AppDto> getApps() {
        return appService.getApps();
    }

    @PostMapping()
    @RequireAdminSecret
    public AppSensitiveDto onboardApp(@RequestBody OnboardAppReqDto req) {
        return appService.onboardApp(req);
    }

    @PutMapping("/{name}/secret/refresh")
    @RequireAppSecret
    public AppSensitiveDto refreshSecret(@PathVariable String name) {
        return appService.refreshSecret(name);
    }
}
