package org.minttwo.exception;

import lombok.NonNull;
import org.minttwo.api.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int NOT_FOUND_STATUS_CODE = 404;

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> handleUnsupportedOperation(@NonNull NotFoundException exception) {
        ErrorDto errorDto = buildErrorDto(exception, NOT_FOUND_STATUS_CODE);
        return ResponseEntity
                .status(NOT_FOUND_STATUS_CODE)
                .body(errorDto);
    }

    private ErrorDto buildErrorDto(@NonNull Exception exception, int code) {
        return ErrorDto.builder()
                .message(exception.getMessage())
                .code(code)
                .build();
    }
}
