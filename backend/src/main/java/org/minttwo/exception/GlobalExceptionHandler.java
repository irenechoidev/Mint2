package org.minttwo.exception;

import lombok.NonNull;
import org.minttwo.api.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int ACCESS_DENIED_STATUS_CODE = 403;
    private static final int NOT_FOUND_STATUS_CODE = 404;

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(@NonNull BadRequestException exception) {
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

    private ErrorDto buildErrorDto(@NonNull Exception exception, int code) {
        return ErrorDto.builder()
                .message(exception.getMessage())
                .code(code)
                .build();
    }
}
