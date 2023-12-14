package com.wtf.cauth.context;

import com.wtf.cauth.util.RandGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION = "authorization";
    private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";

    private static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        final Map<String, String> headersInfoMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headersInfoMap.put(key, value);
        }
        return headersInfoMap;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Map<String, String> headersMap = getHeadersInfo(request);
        MDC.clear();
        setStaticFields(headersMap, request);
        buildContext(headersMap, request);
        log.info("received request");
        return true;
    }

    private void buildContext(Map<String, String> headersMap, HttpServletRequest request) {
        long startTime = DateTime.now().getMillis();
        MDC.put("start", String.valueOf(startTime));
        if (headersMap != null && !headersMap.isEmpty()) {
            String tid = headersMap.get("tid");
            String forwardingIp = headersMap.get(HEADER_X_FORWARDED_FOR);


            if (!StringUtils.hasLength(tid)) {
                tid = RandGeneratorUtil.generate();
            }
            MDC.put("tid", tid);

            if (StringUtils.hasLength(forwardingIp)) {
                MDC.put("forwardingIp", forwardingIp);
            }
        }
    }

    private void setStaticFields(Map<String, String> headersMap, HttpServletRequest request) {
        MDC.put("inTime", String.valueOf(DateTime.now().getMillis()));
        MDC.put("api", request.getRequestURI());
        MDC.put("method", request.getMethod());
        String token = request.getHeader(AUTHORIZATION);
        if (token != null)
            MDC.put("token", token);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String status = response.getStatus() >= 400 ? "FAILURE" : "SUCCESS";
        long runningTime = DateTime.now().getMillis() - new DateTime(Long.valueOf(MDC.get("inTime"))).getMillis();
        log.info("status={} responseCode={} runningTime={}", status, response.getStatus(), runningTime);
    }
}
