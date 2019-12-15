package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> admin = Optional.ofNullable(userRepository
                .findByUsernameOrEmail("admin", "admin@mail.com"));
        if (!admin.isPresent()) {
            User user = new User.Builder()
                    .withUsername("admin")
                    .withEmail("admin@mail.com")
                    .withPassword(passwordEncoder.encode("abc"))
                    .withRole(UserRole.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }
}
