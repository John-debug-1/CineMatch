package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PersonController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/person/{id}")
    public String personDetails(
            @PathVariable int id,
            Model model,
            HttpSession session
    ) {
        Map<String, Object> person = tmdbService.getPersonDetails(id);

        model.addAttribute("person", person);

        // Add logged user to view (if exists)
        Object loggedUser = session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        return "person-details";
    }
}
