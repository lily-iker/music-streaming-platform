package music.configuration;

import music.model.User;
import music.repository.UserRepository;
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
     *   username: ${username}
     *   password: ${username}
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
