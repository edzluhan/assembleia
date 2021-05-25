package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.model.VotingSessionReport;
import com.eduardozluhan.assembly.repository.VoteRepository;
import com.eduardozluhan.assembly.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.eduardozluhan.assembly.repository.VoteRepository.NO;
import static com.eduardozluhan.assembly.repository.VoteRepository.YES;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceTest {

    @Mock
    VoteRepository voteRepository;

    @Mock
    VotingSessionRepository votingSessionRepository;

    @InjectMocks
    VotingSessionService service;

    @Test
    void openVotingSession_shouldSetEndsAtAccordingToRequest() {
        long subjectId = 1L;
        long endsIn = 5L;
        VotingSession votingSession = new VotingSession(1L, now().plusMinutes(5L).truncatedTo(ChronoUnit.MINUTES));

        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(votingSession);

        service.openVotingSession(subjectId, endsIn);

        verify(votingSessionRepository).save(eq(votingSession));
    }

    @Test
    void openVotingSession_shouldSetEndsAtToOneMinuteAfterWhenNotSpecified() {
        long subjectId = 1L;
        VotingSession votingSession = new VotingSession(1L, now().plusMinutes(1L).truncatedTo(ChronoUnit.MINUTES));

        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(votingSession);

        service.openVotingSession(subjectId, null);

        verify(votingSessionRepository).save(eq(votingSession));
    }

    @Test
    void sessionReport_shouldPassSubjectWhenMostVotesAreInFavor() throws VotingSessionNotAvailableException {
        String expectedResult = "Subject approved with 500 votes in favor and 200 votes against";
        Long votingSessionId = 1L;
        Long expectedVotesInFavor = 500L;
        Long expectedVotesAgainst = 200L;
        Long expectedVotesCount = 700L;
        VotingSession votingSessionMock = mock(VotingSession.class);

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.of(votingSessionMock));
        when(voteRepository.countVotesByValueForSession(eq(YES), any())).thenReturn(expectedVotesInFavor);
        when(voteRepository.countVotesByValueForSession(eq(NO), any())).thenReturn(expectedVotesAgainst);

        VotingSessionReport report = service.sessionReport(votingSessionId);

        assertEquals(expectedVotesCount, report.getVotesCount());
        assertEquals(expectedVotesInFavor, report.getVotesInFavor());
        assertEquals(expectedVotesAgainst, report.getVotesAgainst());
        assertEquals(expectedResult, report.getResult());
    }

    @Test
    void sessionReport_shouldRejectSubjectWhenMostVotesAreAgainst() throws VotingSessionNotAvailableException {
        String expectedResult = "Subject rejected with 500 votes against and 200 votes in favor";
        Long votingSessionId = 1L;
        Long expectedVotesInFavor = 200L;
        Long expectedVotesAgainst = 500L;
        Long expectedVotesCount = 700L;
        VotingSession votingSessionMock = mock(VotingSession.class);

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.of(votingSessionMock));
        when(voteRepository.countVotesByValueForSession(eq(YES), any())).thenReturn(expectedVotesInFavor);
        when(voteRepository.countVotesByValueForSession(eq(NO), any())).thenReturn(expectedVotesAgainst);

        VotingSessionReport report = service.sessionReport(votingSessionId);

        assertEquals(expectedVotesCount, report.getVotesCount());
        assertEquals(expectedVotesInFavor, report.getVotesInFavor());
        assertEquals(expectedVotesAgainst, report.getVotesAgainst());
        assertEquals(expectedResult, report.getResult());
    }

    @Test
    void sessionReport_shouldRejectSubjectWhenVotesAreTied() throws VotingSessionNotAvailableException {
        String expectedResult = "Subject rejected with 500 votes against and 500 votes in favor";
        Long votingSessionId = 1L;
        Long expectedVotesInFavor = 500L;
        Long expectedVotesAgainst = 500L;
        Long expectedVotesCount = 1000L;
        VotingSession votingSessionMock = mock(VotingSession.class);

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.of(votingSessionMock));
        when(voteRepository.countVotesByValueForSession(eq(YES), any())).thenReturn(expectedVotesInFavor);
        when(voteRepository.countVotesByValueForSession(eq(NO), any())).thenReturn(expectedVotesAgainst);

        VotingSessionReport report = service.sessionReport(votingSessionId);

        assertEquals(expectedVotesCount, report.getVotesCount());
        assertEquals(expectedVotesInFavor, report.getVotesInFavor());
        assertEquals(expectedVotesAgainst, report.getVotesAgainst());
        assertEquals(expectedResult, report.getResult());
    }

    @Test
    void sessionReport_shouldThrowExceptionWhenSessionNotFound() {
        Long votingSessionId = 1L;

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.empty());

        assertThrows(VotingSessionNotAvailableException.class, () -> service.sessionReport(votingSessionId));
    }
}