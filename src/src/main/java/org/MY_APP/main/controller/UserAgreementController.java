package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAgreementController {

    @PostMapping("/agreement/accept")
    public String accept(HttpSession session) {
        session.removeAttribute("needsAgreement"); // το κλείνει για αυτό το session
        return "ok";
    }
}
