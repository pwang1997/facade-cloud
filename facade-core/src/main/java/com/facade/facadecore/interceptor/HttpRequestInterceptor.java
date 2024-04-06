package com.facade.facadecore.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Puck Wang
 * @project Blog
 * @created 2/24/2024
 */

@Component
@Slf4j
public class HttpRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // This method is called before the controller method is invoked
        log.debug("Request URL::" + request.getRequestURL().toString() + " Sent to Handler :: " + handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // This method is called after the controller method is invoked, but before the view is rendered
        log.debug("Request URL::" + request.getRequestURL().toString() + " Handler returned :: " + handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // This method is called after the view is rendered
        log.debug("Request URL::" + request.getRequestURL().toString() + " Completed");
    }

}
