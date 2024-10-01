package org.minttwo.exception;

import jakarta.annotation.Nullable;
import lombok.NonNull;

public class NotFoundException extends RuntimeException {
    public NotFoundException(@NonNull String message, @Nullable Exception e) {
        super(message, e);
    }
}
