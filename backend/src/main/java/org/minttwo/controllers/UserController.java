package org.minttwo.controllers;

import org.minttwo.dataclients.UserClient;
import org.minttwo.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void createUser(User user) {
        String password = user.getPassword();
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        userClient.createUser(user);
    }
}
