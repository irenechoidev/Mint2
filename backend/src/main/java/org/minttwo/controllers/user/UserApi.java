package org.minttwo.controllers.user;

import org.minttwo.api.user.GetUserResponseDto;
import org.minttwo.api.user.LoginUserRequestDto;
import org.minttwo.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {
    @PostMapping("/")
    public ResponseEntity<Void> createUser(@RequestBody User user);

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponseDto> getUser(@PathVariable String id);

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(
            @RequestBody LoginUserRequestDto requestDto
    );
}
