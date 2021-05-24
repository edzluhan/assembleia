package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.repository.VoteRepository;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class VotingSessionService {
    private final VotingSessionRepository repository;
    private final VoteRepository voteRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(VotingSessionService.class);

    public VotingSessionService(VotingSessionRepository repository, VoteRepository voteRepository) {
        this.repository = repository;
        this.voteRepository = voteRepository;
    }

    public VotingSession openVotingSession(Long subjectId, LocalDateTime endsAt) {
        LocalDateTime endDateTime = calculateEndDateTime(endsAt);
        VotingSession votingSession = new VotingSession(subjectId, endDateTime);

        scheduleVotingSessionEnd(endDateTime, votingSession);

        VotingSession persistedSession = repository.save(votingSession);

        LOGGER.info(format("Session with id %s ending %s was created.", persistedSession.getId(),
                persistedSession.getEndsAt()));

        return persistedSession;
    }

    private void scheduleVotingSessionEnd(LocalDateTime endDateTime, VotingSession votingSession) {
        VotingSessionCloserTask votingSessionCloserTask = new VotingSessionCloserTask(votingSession, voteRepository);
        new Timer().schedule(votingSessionCloserTask,
                Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private LocalDateTime calculateEndDateTime(LocalDateTime endsAt) {
        return endsAt == null ? LocalDateTime.now().plus(1, MINUTES) : endsAt;
    }
}
