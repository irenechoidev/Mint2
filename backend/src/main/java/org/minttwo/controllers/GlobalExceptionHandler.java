package org.minttwo.controllers;

import lombok.NonNull;
import org.minttwo.data.exception.AccessDeniedException;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.generated.api.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int ACCESS_DENIED_STATUS_CODE = 403;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;

    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(@NonNull InvalidInputException exception) {
        ErrorDto errorDto = buildErrorDto(exception, BAD_REQUEST_STATUS_CODE);
        return ResponseEntity
                .status(BAD_REQUEST_STATUS_CODE)
                .body(errorDto);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(@NonNull AccessDeniedException exception) {
        ErrorDto errorDto = buildErrorDto(exception, ACCESS_DENIED_STATUS_CODE);
        return ResponseEntity
                .status(ACCESS_DENIED_STATUS_CODE)
                .body(errorDto);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(@NonNull NotFoundException exception) {
        ErrorDto errorDto = buildErrorDto(exception, NOT_FOUND_STATUS_CODE);
        return ResponseEntity
                .status(NOT_FOUND_STATUS_CODE)
                .body(errorDto);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> handleException(@NonNull Exception exception) {
        ErrorDto errorDto = buildErrorDto(exception, INTERNAL_SERVER_ERROR_STATUS_CODE);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR_STATUS_CODE)
                .body(errorDto);
    }

    private ErrorDto buildErrorDto(@NonNull Exception exception, int code) {
        return ErrorDto.builder()
                .message(exception.getMessage())
                .statusCode(code)
                .build();
    }
}
