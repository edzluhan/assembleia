package com.eduardozluhan.assembly.service;

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

    public Vote registerVote(Vote vote) throws VotingSessionNotAvailableException, UserAlreadyVotedException {
        Optional<VotingSession> votingSessionOptional = votingSessionRepository
                .findById(vote.getVotingSessionId());

        if (isVotingSessionAvailable(votingSessionOptional)) {
            try {
                return voteRepository.save(vote);
            } catch (DataIntegrityViolationException e) {
                throw new UserAlreadyVotedException(vote.getUserId());
            }
        }

        throw new VotingSessionNotAvailableException(vote.getVotingSessionId());
    }

    private boolean isVotingSessionAvailable(Optional<VotingSession> votingSessionOptional) {
        return votingSessionOptional.isPresent() && votingSessionOptional.get().getEndsAt().isAfter(LocalDateTime.now());
    }
}
