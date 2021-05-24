package com.eduardozluhan.assembly.model;

import lombok.Value;

@Value
public class VotingSessionReport {
    Long yesVotes;
    Long noVotes;

    public Long totalVotes() {
        return yesVotes + noVotes;
    }

    public String result() {
        if (yesVotes > noVotes) {
            return String.format("Pauta aprovada com %s votos favoráveis e %s votos contrários", yesVotes, noVotes);
        } else {
            return String.format("Pauta reprovada com %s votos contrários e %s votos favoráveis", noVotes, yesVotes);
        }
    }
}
