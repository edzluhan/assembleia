package com.eduardozluhan.assembly.controller.request;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class OpenVotingSessionRequest {
    @NotNull
    Long subjectId;
    @Positive
    Long durationInMinutes;
}
