package com.eduardozluhan.assembly.controller.request;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class VoteRequest {
    @NotNull
    String userId;
    @NotNull
    Long votingSessionId;
    @NotNull
    String voteValue;
}


