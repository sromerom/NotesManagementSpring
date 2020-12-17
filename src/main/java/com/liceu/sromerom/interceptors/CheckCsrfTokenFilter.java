package com.liceu.sromerom.interceptors;

import com.google.common.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CheckCsrfTokenFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (request.getMethod().equalsIgnoreCase("POST")) {
            String tokenFromRequest = request.getParameter("_csrftoken");
            Cache<String, Boolean> tokenCache = (Cache<String, Boolean>) session.getAttribute("tokenCache");
            if ((tokenCache == null) || (tokenCache.getIfPresent(tokenFromRequest) == null)) {
                response.sendRedirect(request.getContextPath() + "/home");
                return false;
            }
        }
        return true;
    }
}
