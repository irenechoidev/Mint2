package org.minttwo.controllers;

import org.minttwo.dataclients.UserClient;
import org.minttwo.models.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public void createUser(User user) {
        userClient.createUser(user);
    }

    @Override
    public User getUser(String id) {
        return userClient.getUserById(id);
    }
}
