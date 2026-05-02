package org.minttwo.data.exception;

import jakarta.annotation.Nullable;
import lombok.NonNull;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(@NonNull String message, @Nullable Exception e) {
        super(message, e);
    }
}
