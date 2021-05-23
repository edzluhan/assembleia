package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.Application;
import com.eduardozluhan.assembly.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class SubjectControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void createSubject_shouldReturn201WithSubjectId() throws Exception {
        mockMvc.perform(post("/subject")
        .content("{\n" +
                "    \"title\": \"title for test subject\",\n" +
                "    \"details\": \"some details\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("title for test subject"))
        .andExpect(jsonPath("$.details").value("some details"));
    }
}