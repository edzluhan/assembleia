package com.eduardozluhan.assembly.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAlreadyVotedException extends Throwable {
    private final String userId;

    @Override
    public String getMessage() {
        return String.format("User with id %s already voted for this session.", userId);
    }
}
