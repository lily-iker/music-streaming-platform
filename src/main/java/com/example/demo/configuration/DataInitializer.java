package com.example.demo.configuration;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Initializes user passwords for accounts that have empty or null passwords.
     * Each user's password will be set to their username (encrypted).
     *
     * Default accounts:
     * - Admin account:
     *   username: adminadmin
     *   password: adminadmin
     * - Other accounts:
     *   password: same as username
     */
    @Transactional
    @Override
    public void run(String... args) {
        List<User> usersWithEmptyPasswords = userRepository.findByPasswordIsNullOrPasswordIsEmpty();
        if (!usersWithEmptyPasswords.isEmpty()) {
            usersWithEmptyPasswords.forEach(user -> user.setPassword(passwordEncoder.encode(user.getUsername())));
            userRepository.saveAll(usersWithEmptyPasswords);
        }
    }
}
