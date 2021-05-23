package com.eduardozluhan.assembly.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResourceAlreadyExistsException extends Throwable {
    private final String resourceName;

    @Override
    public String getMessage() {
        return String.format("%s already exists.", resourceName);
    }
}
