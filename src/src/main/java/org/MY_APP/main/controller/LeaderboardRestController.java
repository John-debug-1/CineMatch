package org.MY_APP.main.controller;

import org.MY_APP.main.model.User;
import org.MY_APP.main.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardRestController {

    private final UserService userService;

    public LeaderboardRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getLeaderboard() {
        return userService.getLeaderboard();
    }
}