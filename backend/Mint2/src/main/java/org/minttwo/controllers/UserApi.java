package org.minttwo.controllers;

import org.minttwo.models.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {
    @PostMapping("/")
    public void createUser(@RequestBody User user);
}
