package org.minttwo.data.exception;

import jakarta.annotation.Nullable;
import lombok.NonNull;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(@NonNull String message, @Nullable Exception e) {
        super(message, e);
    }
}
