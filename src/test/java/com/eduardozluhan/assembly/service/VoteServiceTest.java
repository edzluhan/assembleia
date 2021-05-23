package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.controller.request.VoteRequest;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.repository.VoteRepository;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @Mock
    VotingSessionRepository votingSessionRepository;

    @Mock
    VoteRepository voteRepository;

    @InjectMocks
    VoteService voteService;

    @Test
    void shouldRegisterVote() throws VotingSessionNotAvailableException, UserAlreadyVotedException {
        VotingSession votingSession = mock(VotingSession.class);

        when(votingSession.getEndsAt()).thenReturn(LocalDateTime.now().plusMinutes(5L));
        when(votingSessionRepository.findById(any())).thenReturn(Optional.of(votingSession));

        VoteRequest request = new VoteRequest("uniqueUserId", 1L, "Sim");

        voteService.registerVote(request);

        verify(voteRepository).save(any());
    }

    @Test
    void shouldNotRegisterVote_whenVotingSessionIsEnded() {
        VotingSession votingSession = mock(VotingSession.class);

        when(votingSession.getEndsAt()).thenReturn(LocalDateTime.now().minusMinutes(5L));
        when(votingSessionRepository.findById(any())).thenReturn(Optional.of(votingSession));

        VoteRequest request = new VoteRequest("uniqueUserId", 1L, "Sim");

        assertThrows(VotingSessionNotAvailableException.class,() -> voteService.registerVote(request));

        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldNotRegisterVote_whenVotingSessionIsNotFound() {
        when(votingSessionRepository.findById(any())).thenReturn(Optional.empty());

        VoteRequest request = new VoteRequest("uniqueUserId", 1L, "Sim");

        assertThrows(VotingSessionNotAvailableException.class,() -> voteService.registerVote(request));

        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldNotRegisterVote_whenUserAlreadyVoted() {
        VotingSession votingSession = mock(VotingSession.class);

        when(votingSession.getEndsAt()).thenReturn(LocalDateTime.now().plusMinutes(5L));
        when(votingSessionRepository.findById(any())).thenReturn(Optional.of(votingSession));
        when(voteRepository.save(any())).thenThrow(new DataIntegrityViolationException("some message"));

        VoteRequest request = new VoteRequest("uniqueUserId", 1L, "Sim");

        assertThrows(UserAlreadyVotedException.class,() -> voteService.registerVote(request));
    }
}
