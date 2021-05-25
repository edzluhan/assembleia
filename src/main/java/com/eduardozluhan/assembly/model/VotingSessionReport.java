package com.eduardozluhan.assembly.model;

import lombok.Value;

@Value
public class VotingSessionReport {
    Long votesInFavor;
    Long votesAgainst;
    Long votesCount;
    String result;
}
