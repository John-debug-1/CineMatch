package org.MY_APP.main.service;

import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // SIGN UP
    public boolean createUser(String email, String password, String fullName) {

        // έλεγχος αν υπάρχει ήδη email
        if (userRepository.findByEmail(email) != null) {
            return false;
        }

        User user = new User(email, password, fullName);
        userRepository.save(user);
        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username) {
        // Στα περισσότερα setups, το Principal.getName() είναι το email
        return userRepository.findByEmail(username);
    }

    public boolean validateLogin(String email, String password) {
        User user = findByEmail(email);
        if (user == null) return false;
        return user.getPassword().equals(password);
    }

    // ✅ ✅ ✅ LEADERBOARD
    public List<User> getLeaderboard() {
        return userRepository.findTop10ByOrderByTotalQuizScoreDesc();
    }
}
