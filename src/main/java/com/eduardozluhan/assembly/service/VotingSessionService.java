package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class VotingSessionService {
    private final VotingSessionRepository repository;

    public VotingSessionService(VotingSessionRepository repository) {
        this.repository = repository;
    }

    public VotingSession openVotingSession(Integer subjectId, LocalDateTime endsAt) {
        LocalDateTime endDateTime = calculateEndDateTime(endsAt);
        VotingSession votingSession = new VotingSession(subjectId, endDateTime);

        scheduleVotingSessionEnd(endDateTime, votingSession);

        return repository.save(votingSession);
    }

    private void scheduleVotingSessionEnd(LocalDateTime endDateTime, VotingSession votingSession) {
        VotingSessionTask votingSessionTask = new VotingSessionTask(votingSession);
        new Timer().schedule(votingSessionTask,
                Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private LocalDateTime calculateEndDateTime(LocalDateTime endsAt) {
        return endsAt == null ? LocalDateTime.now().plus(1, MINUTES) : endsAt;
    }
}
