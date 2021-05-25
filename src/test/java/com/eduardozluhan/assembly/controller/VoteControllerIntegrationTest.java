package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.Application;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.Vote;
import com.eduardozluhan.assembly.service.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class VoteControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    VoteService service;

    @Test
    void shouldRespond201WithVote() throws Exception, UserAlreadyVotedException, VotingSessionNotAvailableException {
        LocalDateTime votedAt = LocalDateTime.now();
        when(service.registerVote(any()))
                .thenReturn(new Vote(1L, "unique user id", 1L, "SIM", votedAt));


        mockMvc.perform(post("/vote")
                .content("{\n" +
                        "    \"userId\": \"unique user id\",\n" +
                        "    \"votingSessionId\": 1,\n" +
                        "    \"voteValue\": \"SIM\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse(votedAt)));
    }

    @Test
    void shouldRespond400WhenVoteValueIsInvalid() throws Exception {
        mockMvc.perform(post("/vote")
                .content("{\n" +
                        "    \"userId\": \"unique user id\",\n" +
                        "    \"votingSessionId\": 1,\n" +
                        "    \"voteValue\": \"yes\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Vote values must be \"Sim\" or \"Não\""));
    }

    @Test
    void shouldRespond409WhenUserAlreadyVoted() throws Exception, UserAlreadyVotedException, VotingSessionNotAvailableException {
        when(service.registerVote(any())).thenThrow(new UserAlreadyVotedException("unique user id"));

        mockMvc.perform(post("/vote")
                .content("{\n" +
                        "    \"userId\": \"unique user id\",\n" +
                        "    \"votingSessionId\": 1,\n" +
                        "    \"voteValue\": \"SIM\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string("User with id unique user id already voted for this session."));
    }

    @Test
    void shouldRespond404WhenVotingSessionIsNotAvailable() throws Exception, UserAlreadyVotedException, VotingSessionNotAvailableException {
        LocalDateTime votedAt = LocalDateTime.now();
        when(service.registerVote(any())).thenThrow(new VotingSessionNotAvailableException(1L));

        mockMvc.perform(post("/vote")
                .content("{\n" +
                        "    \"userId\": \"unique user id\",\n" +
                        "    \"votingSessionId\": 1,\n" +
                        "    \"voteValue\": \"SIM\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Voting session with id 1 is not available"));
    }

    private String jsonResponse(LocalDateTime votedAt) {
        return format("{\n" +
                "    \"id\": 1,\n" +
                "    \"userId\": \"unique user id\",\n" +
                "    \"votingSessionId\": 1,\n" +
                "    \"voteValue\": \"SIM\",\n" +
                "    \"votedAt\": \"%s\"\n" +
                "}", votedAt);
    }
}