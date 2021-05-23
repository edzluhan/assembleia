package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.model.VotingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class VotingSessionTask extends TimerTask {
    private final Logger LOGGER = LoggerFactory.getLogger(VotingSessionTask.class);
    private final VotingSession votingSession;

    public VotingSessionTask(VotingSession votingSession) {
        this.votingSession = votingSession;
    }

    @Override
    public void run() {
        LOGGER.info(String.format("------------------- Voting session %s ended at %s -------------------",
                votingSession.getId(), votingSession.getEndsAt()));
    }
}
