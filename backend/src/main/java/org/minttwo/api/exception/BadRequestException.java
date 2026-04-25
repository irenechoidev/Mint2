package org.minttwo.api.exception;

import jakarta.annotation.Nullable;
import lombok.NonNull;

public class BadRequestException extends RuntimeException {
    public BadRequestException(@NonNull String message, @Nullable Exception e) {
        super(message, e);
    }
}
