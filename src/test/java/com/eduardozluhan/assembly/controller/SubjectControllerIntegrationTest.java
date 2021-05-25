package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.Application;
import com.eduardozluhan.assembly.exceptions.ResourceAlreadyExistsException;
import com.eduardozluhan.assembly.model.Subject;
import com.eduardozluhan.assembly.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class SubjectControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService service;

    @Test
    void shouldRespond201WithSubjectId() throws Exception, ResourceAlreadyExistsException {
        when(service.storeSubject(any()))
                .thenReturn(new Subject(1L, "title for test subject", "some details"));

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

    @Test
    void shouldRespond409WhenSubjectAlreadyExists() throws ResourceAlreadyExistsException, Exception {
        when(service.storeSubject(any()))
                .thenThrow(new ResourceAlreadyExistsException("Subject"));

        mockMvc.perform(post("/subject")
                .content("{\n" +
                        "    \"title\": \"title for test subject\",\n" +
                        "    \"details\": \"some details\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
        .andExpect(content().string("Subject already exists."));
    }
}