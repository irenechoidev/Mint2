package org.minttwo.controllers.user;

import org.minttwo.api.user.LoginUserRequestDto;
import org.minttwo.api.user.adapters.UserAdapter;
import org.minttwo.api.user.GetUserResponseDto;
import org.minttwo.api.user.UserDto;
import org.minttwo.dataclients.UserClient;
import org.minttwo.exception.AccessDeniedException;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserClient userClient;
    private final UserAdapter userAdapter;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
        this.userAdapter = new UserAdapter();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public ResponseEntity<Void> createUser(User user) {
        userClient.createUser(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetUserResponseDto> getUser(String id) {
        User user = userClient.getUserById(id);
        UserDto userDto = userAdapter.adapt(user);
        GetUserResponseDto getUserDto = GetUserResponseDto.builder()
                    .user(userDto)
                    .build();
        return ResponseEntity.ok(getUserDto);
    }

    @Override
    public ResponseEntity<Void> loginUser(LoginUserRequestDto requestDto) {
        User user = userClient.getByUsername(requestDto.getUsername());
        if (user == null) {
            String errMessage = "User with username %s not found";
            throw new NotFoundException(String.format(errMessage, requestDto.getUsername()), null);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            String errMessage = "Password for user with username %s is incorrect";
            throw new AccessDeniedException(String.format(errMessage, requestDto.getUsername()), null);
        }

        return ResponseEntity.ok().build();
    }
}
