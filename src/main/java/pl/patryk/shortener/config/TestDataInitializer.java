package pl.patryk.shortener.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.patryk.shortener.entity.User;
import pl.patryk.shortener.repository.UserRepository;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(TestDataInitializer.class);

    @PostConstruct
    public void init() {
        String email = "admin@admin.pl";
        String password = "pass";
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            userRepository.save(user);
            log.info("Test user created: email: {}, password: {}", email, password);
        }
    }
}
