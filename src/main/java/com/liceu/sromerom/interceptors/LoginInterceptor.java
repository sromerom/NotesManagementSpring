package com.liceu.sromerom.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;


    private static final String[] loginRequiredURLs = new String[]{"/home", "/unlogin", "/create", "/edit", "/delete", "/deleteAllShare", "/deleteShare", "/share", "/detail", "/users"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //HttpSession session = request.getSession();

        String urlLogin = request.getContextPath() + "/login";
        Long userid = (Long) session.getAttribute("userid");
        Map<String, String> userDetails = (Map<String, String>) session.getAttribute("userDetails");

        
        System.out.println("userid en el interceptor: " + userid);
        //No ha fet login y a damunt vol entrar en la part privada
        if (userid == null && needLogin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        System.out.println(request.getRequestURI());
        //L'usuari ya ha fet login i esta en la pagina login, per tant, ho duim a la pagina principal per que ja esta autenticat i no pinta res en el login
        if (userid != null && request.getRequestURI().equals(urlLogin)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return false;
        }

        //Si s'arriba fins aqui, voldra dir dues coses:
        // 1.- Que l'usuari ha fet login y es troba en una pagina que no es el login.
        // 2.- L'usuari es la primera vegada que entra y no te cap sessio iniciada, per tant en la part del login ho deixarem passar

        return true;
    }

    private boolean needLogin(HttpServletRequest req) {
        String actualURL = req.getRequestURL().toString();

        for (String loginRequiredURL : loginRequiredURLs) {
            if (actualURL.contains(loginRequiredURL)) {
                return true;
            }
        }

        return false;
    }
}
