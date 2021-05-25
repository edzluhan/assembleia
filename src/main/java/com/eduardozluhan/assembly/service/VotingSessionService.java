package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.model.VotingSessionReport;
import com.eduardozluhan.assembly.repository.VoteRepository;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

import static com.eduardozluhan.assembly.repository.VoteRepository.NO;
import static com.eduardozluhan.assembly.repository.VoteRepository.YES;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;
    private final VoteRepository voteRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(VotingSessionService.class);

    public VotingSessionService(VotingSessionRepository votingSessionRepository, VoteRepository voteRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.voteRepository = voteRepository;
    }

    public VotingSession openVotingSession(Long subjectId, LocalDateTime endsAt) {
        LocalDateTime endDateTime = calculateEndDateTime(endsAt);
        VotingSession votingSession = new VotingSession(subjectId, endDateTime);

        scheduleVotingSessionEnd(endDateTime, votingSession);

        VotingSession persistedSession = votingSessionRepository.save(votingSession);

        LOGGER.info(format("Session with id %s ending %s was created.", persistedSession.getId(),
                persistedSession.getEndsAt()));

        return persistedSession;
    }

    public VotingSessionReport sessionReport(Long votingSessionId) throws VotingSessionNotAvailableException {

        if (votingSessionRepository.findById(votingSessionId).isEmpty()) {
            throw new VotingSessionNotAvailableException(votingSessionId);
        }

        Long votesInFavor = voteRepository.countVotesByValueForSession(YES, votingSessionId);
        Long votesAgainst = voteRepository.countVotesByValueForSession(NO, votingSessionId);
        Long votesCount = votesInFavor + votesAgainst;

        String result = votesInFavor > votesAgainst ?
                format("Subject approved with %s votes in favor and %s votes against", votesInFavor, votesAgainst) :
                format("Subject rejected with %s votes against and %s votes in favor", votesAgainst, votesInFavor);

        return new VotingSessionReport(votesInFavor, votesAgainst, votesCount, result);
    }

    private void scheduleVotingSessionEnd(LocalDateTime endDateTime, VotingSession votingSession) {
        VotingSessionCloserTask votingSessionCloserTask = new VotingSessionCloserTask(votingSession);
        new Timer().schedule(votingSessionCloserTask,
                Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private LocalDateTime calculateEndDateTime(LocalDateTime endsAt) {
        return endsAt == null ? LocalDateTime.now().plus(1, MINUTES) : endsAt;
    }
}
