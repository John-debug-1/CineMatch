package org.MY_APP.main;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@Component
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addLoggedUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            model.addAttribute("loggedUser", user);
        }
    }
}
