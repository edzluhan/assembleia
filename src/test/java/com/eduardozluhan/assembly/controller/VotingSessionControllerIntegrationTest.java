package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.Application;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.service.VotingSessionService;
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
class VotingSessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotingSessionService service;

    @Test
    void shouldRespond201WithVotingSessionEndingIn1Minute() throws Exception {
        LocalDateTime endsAt = LocalDateTime.now().plusMinutes(1);
        when(service.openVotingSession(any(), any()))
                .thenReturn(new VotingSession(1L, 1L, endsAt));


        mockMvc.perform(post("/voting-session")
                .content("{\n" +
                        "    \"subjectId\": 1\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse(endsAt)));
    }

    @Test
    void shouldRespond201WithVotingSessionEndingAfterRequestedMinutes() throws Exception {
        LocalDateTime endsAt = LocalDateTime.now().plusMinutes(5L);
        when(service.openVotingSession(any(), any()))
                .thenReturn(new VotingSession(1L, 1L, endsAt));

        mockMvc.perform(post("/voting-session")
                .content("{\n" +
                        "    \"subjectId\": 1,\n" +
                        "    \"endsIn\": 5\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse(endsAt)));
    }

    private String jsonResponse(LocalDateTime endsAt) {
        return format("{\n" +
                "    \"id\": 1,\n" +
                "    \"subjectId\": 1,\n" +
                "    \"endsAt\": \"%s\"\n" +
                "}", endsAt);
    }
}