package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LegalController {

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @PostMapping("/terms/accept")
    public String accept(HttpSession session) {
        session.setAttribute("termsAccepted", true);
        return "redirect:/home";
    }

    @GetMapping("/terms/decline")
    public String decline(HttpSession session) {
        session.invalidate(); // κλείνει session
        return "redirect:/terms";
        // (εναλλακτικά: redirect σε /login ή σε σελίδα "access denied")
    }
}
