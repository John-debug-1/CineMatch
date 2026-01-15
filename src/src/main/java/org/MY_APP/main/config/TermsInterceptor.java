package org.MY_APP.main.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TermsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String path = request.getRequestURI();

        // Άφησε ελεύθερα τα terms endpoints + static resources
        if (path.startsWith("/terms")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")
                || path.startsWith("/uploads")
                || path.startsWith("/webjars")) {
            return true;
        }

        HttpSession session = request.getSession(true); // δημιουργεί session αν δεν υπάρχει
        Boolean accepted = (Boolean) session.getAttribute("termsAccepted");

        // Αν δεν έχει δεχτεί ακόμα, πήγαινε terms
        if (accepted == null || !accepted) {
            response.sendRedirect("/terms");
            return false;
        }

        return true;
    }
}
