package org.MY_APP.main.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        //  Αν ΔΕΝ υπάρχει loggedUser → redirect στο /login
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true; //  συνεχίζει κανονικά
    }
}
