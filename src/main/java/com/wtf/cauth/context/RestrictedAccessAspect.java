package com.wtf.cauth.context;

import com.wtf.cauth.data.model.App;
import com.wtf.cauth.exception.BadRequestException;
import com.wtf.cauth.exception.UnAuthenticatedException;
import com.wtf.cauth.props.AdminCredentials;
import com.wtf.cauth.service.AppService;
import com.wtf.cauth.util.BCryptUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestrictedAccessAspect {
    private final AppService appService;
    private final AdminCredentials adminCredentials;

    @Around("@annotation(requireAdminScret)")
    public Object verifyAuthentication(ProceedingJoinPoint joinPoint, RequireAdminSecret requireAdminScret) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String secret = request.getHeader("admin_secret");
        if (secret == null)
            throw new BadRequestException("missing admin_secret header");
        if (!adminCredentials.getSecret().equals(secret))
            throw new UnAuthenticatedException("invalid admin secret");
        return joinPoint.proceed();
    }

    @Around("@annotation(requireAppSecret)")
    public Object verifyAuthentication(ProceedingJoinPoint joinPoint, RequireAppSecret requireAppSecret) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String appSecret = request.getHeader("app_secret");
        String appName = request.getHeader("app_name");
        if (appName == null || appSecret == null)
            throw new BadRequestException("missing app_name/app_secret header");
        App app = appService.getAppByName(appName);
        if (!BCryptUtil.verify(appSecret, app.getSecret()))
            throw new UnAuthenticatedException("invalid app id/secret");
        return joinPoint.proceed();
    }
}
