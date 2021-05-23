package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.controller.request.VoteRequest;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.Vote;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.repository.VoteRepository;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    public VoteService(VoteRepository voteRepository, VotingSessionRepository votingSessionRepository) {
        this.voteRepository = voteRepository;
        this.votingSessionRepository = votingSessionRepository;
    }

    public Vote registerVote(VoteRequest request) throws VotingSessionNotAvailableException, UserAlreadyVotedException {
        Vote voteFromRequest = Vote.from(request);

        Optional<VotingSession> votingSessionOptional = votingSessionRepository
                .findById(request.getVotingSessionId());

        if (isVotingSessionAvailable(votingSessionOptional)) {
            try {
                return voteRepository.save(voteFromRequest);
            } catch (DataIntegrityViolationException e) {
                throw new UserAlreadyVotedException(request.getUserId());
            }
        }

        throw new VotingSessionNotAvailableException(request.getVotingSessionId());
    }

    private boolean isVotingSessionAvailable(Optional<VotingSession> votingSessionOptional) {
        return votingSessionOptional.isPresent() && votingSessionOptional.get().getEndsAt().isAfter(LocalDateTime.now());
    }
}
