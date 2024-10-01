package org.minttwo.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDto {
    private String message;
    private int code;
}
