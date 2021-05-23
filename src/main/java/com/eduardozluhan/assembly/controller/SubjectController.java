package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.controller.request.SubjectRequest;
import com.eduardozluhan.assembly.exceptions.ResourceAlreadyExistsException;
import com.eduardozluhan.assembly.model.Subject;
import com.eduardozluhan.assembly.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class SubjectController {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @PostMapping(path = "/subject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Subject> createSubject(@RequestBody @Valid SubjectRequest request) throws ResourceAlreadyExistsException {
        return new ResponseEntity<>(service.storeSubject(request), HttpStatus.CREATED);
    }
}

