package org.MY_APP.main.controller;

import jakarta.servlet.http.HttpSession;
import org.MY_APP.main.model.SearchHistory;
import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SearchHistoryController {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @GetMapping("/history")
    public String historyPage(Model model, HttpSession session) {

        Object loggedUserObj = session.getAttribute("loggedUser");

        if (loggedUserObj == null) {
            return "redirect:/login";
        }

        User loggedUser = (User) loggedUserObj;

        List<SearchHistory> history =
                searchHistoryRepository.findByUserOrderBySearchedAtDesc(loggedUser);

        model.addAttribute("history", history);
        model.addAttribute("loggedUser", loggedUser);

        return "history";
    }
}
