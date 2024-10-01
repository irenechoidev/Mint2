package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.api.GetUserResponseDto;
import org.minttwo.api.UserDto;
import org.minttwo.dataclients.UserClient;
import org.minttwo.models.User;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String TEST_USER_ID = "TEST_USER_ID";

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController subject;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void createUserSuccess() {
        User expectedUser = buildUser();

        subject.createUser(expectedUser);

        verify(userClient, times(1)).createUser(userCaptor.capture());
        User testUser = userCaptor.getValue();

        assertThat(testUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(testUser.getUsername()).isEqualTo(expectedUser.getUsername());
    }

    @Test
    void getUserSuccess() {
        User expectedUser = buildUser();
        when(userClient.getUserById(anyString())).thenReturn(expectedUser);

        ResponseEntity<GetUserResponseDto> response = subject.getUser(TEST_USER_ID);
        UserDto testUser = Optional.ofNullable(response.getBody())
                .map(GetUserResponseDto::getUser)
                .orElse(null);

        assertNotNull(testUser);
        assertThat(testUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(testUser.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(testUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    private User buildUser() {
        return User.builder()
                .email("test@email.com")
                .username("Test-Username")
                .password("Test-Password")
                .build();
    }
}
