package com.facade.facadecore.interceptor;

import com.facade.facadecore.config.SecretConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/15/2024
 */

@Component
@Slf4j
@AllArgsConstructor
public class SecretTokenInterceptor implements HandlerInterceptor {

    private final SecretConfig secretConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.GET.name()) || request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        if (!isAuthorized(request)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return true;
    }

    private boolean isAuthorized(HttpServletRequest request) {
        try {
            log.warn("Header: {}, secret: {}", request.getHeader("Secret"), secretConfig.getSecretToken());
            return request.getHeader("Secret").equals(secretConfig.getSecretToken());
        } catch (NullPointerException e) {
            return false;
        }
    }

}
