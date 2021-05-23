package com.eduardozluhan.assembly.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VotingSessionNotAvailableException extends Throwable {
    Long votingSessionId;

    @Override
    public String getMessage() {
        return String.format("Voting session with id %s is not available", votingSessionId);
    }
}
