package org.minttwo.controllers;

import org.minttwo.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {
    @PostMapping("/")
    public void createUser(@RequestBody User user);

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id);
}
