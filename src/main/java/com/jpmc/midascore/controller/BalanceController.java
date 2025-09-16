package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final UserRepository userRepository;

    @Autowired
    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Balance getBalance(@RequestParam Long userId) {
        // Find user by ID
        UserRecord user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            // Return balance of 0 if user doesn't exist
            return new Balance(0.0f);
        }

        // Return the user's actual balance
        return new Balance(user.getBalance());
    }
}