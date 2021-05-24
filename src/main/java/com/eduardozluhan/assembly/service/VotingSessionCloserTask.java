package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.model.VotingSessionReport;
import com.eduardozluhan.assembly.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class VotingSessionCloserTask extends TimerTask {
    private final Logger LOGGER = LoggerFactory.getLogger(VotingSessionCloserTask.class);
    private final VotingSession votingSession;
    private final VoteRepository voteRepository;

    public VotingSessionCloserTask(VotingSession votingSession, VoteRepository voteRepository) {
        this.votingSession = votingSession;
        this.voteRepository = voteRepository;
    }

    @Override
    public void run() {
        LOGGER.info(String.format("---- Votação com id %s encerrada em %s ----",
                votingSession.getId(), votingSession.getEndsAt()));
        VotingSessionReport votingSessionReport = new VotingSessionReport(
            voteRepository.countVotesByValueForSession(VoteRepository.YES, votingSession.getId()),
            voteRepository.countVotesByValueForSession(VoteRepository.NO, votingSession.getId()));

        LOGGER.info(votingSessionReport.result());
    }
}
