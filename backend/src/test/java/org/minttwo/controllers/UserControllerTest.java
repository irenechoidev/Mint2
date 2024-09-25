package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.controllers.UserController;
import org.minttwo.dataclients.UserClient;
import org.minttwo.models.User;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

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

    private User buildUser() {
        return User.builder()
                .email("test@email.com")
                .username("Test-Username")
                .password("Test-Password")
                .build();
    }
}
