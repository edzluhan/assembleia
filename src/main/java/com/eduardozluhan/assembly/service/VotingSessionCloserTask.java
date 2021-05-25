package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.model.VotingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class VotingSessionCloserTask extends TimerTask {
    private final Logger LOGGER = LoggerFactory.getLogger(VotingSessionCloserTask.class);
    private final VotingSession votingSession;

    public VotingSessionCloserTask(VotingSession votingSession) {
        this.votingSession = votingSession;
    }

    @Override
    public void run() {
        LOGGER.info(String.format("---- Votação com id %s encerrada em %s ----",
                votingSession.getId(), votingSession.getEndsAt()));
    }
}
