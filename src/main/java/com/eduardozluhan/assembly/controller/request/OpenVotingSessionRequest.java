package com.eduardozluhan.assembly.controller.request;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class OpenVotingSessionRequest {
    @NotNull
    Integer subjectId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime endsAt;
}
