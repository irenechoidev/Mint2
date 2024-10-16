package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.api.user.GetUserResponseDto;
import org.minttwo.api.user.LoginUserRequestDto;
import org.minttwo.api.user.UserDto;
import org.minttwo.controllers.user.UserController;
import org.minttwo.dataclients.UserClient;
import org.minttwo.exception.AccessDeniedException;
import org.minttwo.exception.BadRequestException;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.User;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private static final String TEST_USER_ID_PREFIX = "TEST_USER_ID_";

    @Mock
    private UserClient userClient;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void whenCallingCreateUser_BadRequest() {
        User expectedUser = buildUser();
        expectedUser.setUsername("");

        String expectedErrMessage = "Username is required and cannot be blank";

        doThrow(new BadRequestException(expectedErrMessage, null))
                .when(userClient).createUser(any());

        assertThatThrownBy(() -> subject.createUser(expectedUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(expectedErrMessage);
    }

    @Test
    void getUserSuccess() {
        int userNumber = 1;
        String userId = TEST_USER_ID_PREFIX + userNumber;

        User expectedUser = buildUser();
        when(userClient.getUserById(anyString())).thenReturn(expectedUser);

        ResponseEntity<GetUserResponseDto> response = subject.getUser(userId);
        UserDto testUser = Optional.ofNullable(response.getBody())
                .map(GetUserResponseDto::getUser)
                .orElse(null);

        assertNotNull(testUser);
        assertThat(testUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(testUser.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(testUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void whenCallingGetUser_ResourceNotFound() {
        int userNumber = 1;
        String userId = TEST_USER_ID_PREFIX + userNumber;
        String expectedErrMessage = String.format("User with id %s not found", userId);
        when(userClient.getUserById(anyString()))
                .thenThrow(new NotFoundException(expectedErrMessage, null));
        assertThatThrownBy(() -> subject.getUser(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(expectedErrMessage);
    }

    @Test
    void loginUserSuccess() {
        int successStatusCode = 200;
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(successStatusCode);
        User expectedUser = buildUser();

        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .username(expectedUser.getUsername())
                .password(expectedUser.getPassword())
                .build();

        when(userClient.getByUsername(anyString())).thenReturn(expectedUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Void> response = subject.loginUser(requestDto);
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Test
    void whenCallingUserLogin_ResourceNotFound() {
        User expectedUser = buildUser();
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .username(expectedUser.getUsername())
                .password(expectedUser.getPassword())
                .build();

        String expectedErrMessage = String.format(
                "User with username %s not found",
                requestDto.getUsername()
        );

        when(userClient.getByUsername(anyString())).thenReturn(null);

        assertThatThrownBy(() -> subject.loginUser(requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(expectedErrMessage);
    }

    @Test
    void whenCallingUserLogin_AccessDenied() {
        User expectedUser = buildUser();
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .username(expectedUser.getUsername())
                .password(expectedUser.getPassword())
                .build();

        String expectedErrMessage = String.format(
                "Password for user with username %s is incorrect",
                requestDto.getUsername()
        );

        when(userClient.getByUsername(anyString())).thenReturn(expectedUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> subject.loginUser(requestDto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(expectedErrMessage);
    }

    private User buildUser() {
        return User.builder()
                .email("test@email.com")
                .username("Test-Username")
                .password("Test-Password")
                .build();
    }
}
