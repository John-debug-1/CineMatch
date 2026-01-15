package org.MY_APP.main;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadController {

    @GetMapping("/upload")
    public String showUploadPage(HttpSession session, Model model) {

        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));

        return "static/upload"; // same path, now with dynamic user
    }
}
