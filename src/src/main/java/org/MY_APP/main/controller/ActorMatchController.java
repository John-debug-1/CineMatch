package org.MY_APP.main.controller;

import org.MY_APP.main.model.User;
import org.MY_APP.main.service.QuizAiService;
import org.MY_APP.main.service.TmdbService;
import org.MY_APP.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class ActorMatchController {

    @Autowired
    private QuizAiService quizAiService;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private UserService userService;

    @GetMapping("/actor-match")
    public String actorMatchPage(Model model, Principal principal) {
        // Εξασφαλίζουμε ότι ο χρήστης υπάρχει στο μοντέλο για το Navbar
        addUserToModel(model, principal);
        return "actor-match";
    }

    @PostMapping("/actor-match")
    public String handleActorMatch(@RequestParam("photo") MultipartFile photo, Model model, Principal principal) {
        // Προσθήκη χρήστη στην αρχή της μεθόδου
        addUserToModel(model, principal);

        if (photo.isEmpty()) {
            model.addAttribute("error", "Παρακαλώ ανέβασε μια φωτογραφία πρώτα!");
            return "actor-match";
        }

        try {
            byte[] bytes = photo.getBytes();
            String matchedName = quizAiService.findMatchingActor(bytes);

            // Αν η AI αποτύχει, βάζουμε ένα default όνομα για να μην σκάσει το UI
            if (matchedName == null || matchedName.isEmpty()) {
                matchedName = "Unknown Actor";
            }

            String userPhotoBase64 = Base64.getEncoder().encodeToString(bytes);
            model.addAttribute("userPhoto", "data:image/jpeg;base64," + userPhotoBase64);
            model.addAttribute("matchedActorName", matchedName);

            // Αναζήτηση στο TMDB
            try {
                Map<String, Object> searchResponse = tmdbService.search(matchedName);
                if (searchResponse != null && searchResponse.containsKey("results")) {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) searchResponse.get("results");

                    if (results != null && !results.isEmpty()) {
                        Map<String, Object> actor = results.get(0);
                        if (actor.get("profile_path") != null) {
                            model.addAttribute("actorImage", "https://image.tmdb.org/t/p/w500" + actor.get("profile_path"));
                        }
                    }
                }
            } catch (Exception tmdbEx) {
                System.err.println("TMDB Error: " + tmdbEx.getMessage());
            }

            //  ΚΡΙΣΙΜΟ: Ξανακαλούμε την addUserToModel πριν το return για να "κλειδώσει" ο user στο Navbar
            addUserToModel(model, principal);

            return "actor-match-result";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Σφάλμα συστήματος: " + e.getMessage());
            addUserToModel(model, principal); // Προσθήκη και στο error state
            return "actor-match";
        }
    }

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            // Χρησιμοποιούμε το email/username από το principal για να βρούμε τον User στη DB
            User user = userService.findByUsername(principal.getName());
            if (user != null) {
                model.addAttribute("user", user);
                // Debugging: Τύπωσε το όνομα για να το δεις στο console
                System.out.println("User added to model: " + user.getFullName());
            }
        }
    }
}