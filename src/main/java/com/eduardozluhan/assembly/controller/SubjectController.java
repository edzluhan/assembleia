package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.model.Subject;
import com.eduardozluhan.assembly.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class SubjectController {
    private final SubjectRepository repository;

    public SubjectController(SubjectRepository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "/subject")
    ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        Subject persistedSubject = repository.save(subject);
        return new ResponseEntity<>(persistedSubject, HttpStatus.CREATED);
    }
}
