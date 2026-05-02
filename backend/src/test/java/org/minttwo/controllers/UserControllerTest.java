package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.data.dataclients.UserClient;
import org.minttwo.data.exception.AccessDeniedException;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.data.models.UserModel;
import org.minttwo.generated.api.CreateUserDto;
import org.minttwo.generated.api.GetUserResponseDto;
import org.minttwo.generated.api.LoginUserDto;
import org.minttwo.generated.api.UserDto;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController subject;

    @Captor
    private ArgumentCaptor<UserModel> userModelCaptor;

    @Test
    void testCreateUser_200() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("test-username")
                .email("test-email")
                .password("test-password")
                .build();

        subject.createUser(createUserDto);
        verify(userClient, times(1))
                .createUser(userModelCaptor.capture());

        UserModel userModel = userModelCaptor.getValue();
        assertThat(userModel.getEmail()).isEqualTo("test-email");
        assertThat(userModel.getUsername()).isEqualTo("test-username");
    }

    @Test
    void testCreateUser_400() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("test-username")
                .email("test-email")
                .password("test-password")
                .build();

        doThrow(new InvalidInputException("Username is required and cannot be blank", null))
                .when(userClient).createUser(any());

        assertThatThrownBy(() -> subject.createUser(createUserDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Username is required and cannot be blank");
    }

    @Test
    void testGetUser_200() {
        when(userClient.loadById(anyString())).thenReturn(UserModel.builder()
                        .id("id-456")
                        .username("username-233")
                        .email("email-123")
                .build());

        String userId = "id-456";
        ResponseEntity<GetUserResponseDto> response = subject.getUser(userId);
        UserDto userDto = Optional.ofNullable(response.getBody())
                .map(GetUserResponseDto::getUser)
                .orElse(null);

        assertNotNull(userDto);
        assertThat(userDto.getId()).isEqualTo("id-456");
        assertThat(userDto.getUsername()).isEqualTo("username-233");
        assertThat(userDto.getEmail()).isEqualTo("email-123");
    }

    @Test
    void testGetUser_404() {
        when(userClient.loadById(anyString()))
                .thenThrow(new NotFoundException("User with id id-456 not found", null));

        String userId = "id-456";
        assertThatThrownBy(() -> subject.getUser(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id id-456 not found");
    }

    @Test
    void testLoginUser_200() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .username("my-username")
                .password("my-password")
                .build();

        doNothing().when(userClient).loginUser(any());
        ResponseEntity<Void> response = subject.loginUser(loginUserDto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void testUserLogin_404() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .username("my-username")
                .password("my-password")
                .build();

        doThrow(new NotFoundException("User with username my-username not found", null))
                .when(userClient).loginUser(any(UserModel.class));

        assertThatThrownBy(() -> subject.loginUser(loginUserDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with username my-username not found");
    }

    @Test
    void testUserLogin_403() {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .username("my-username")
                .password("my-password")
                .build();

        doThrow(new AccessDeniedException("Password for user with username my-username is incorrect", null))
                .when(userClient).loginUser(any(UserModel.class));

        assertThatThrownBy(() -> subject.loginUser(loginUserDto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Password for user with username my-username is incorrect");
    }
}
