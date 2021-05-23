package com.eduardozluhan.assembly.service;

import com.eduardozluhan.assembly.controller.request.SubjectRequest;
import com.eduardozluhan.assembly.exceptions.ResourceAlreadyExistsException;
import com.eduardozluhan.assembly.model.Subject;
import com.eduardozluhan.assembly.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    private final SubjectRepository repository;
    private final Logger LOGGER = LoggerFactory.getLogger(SubjectService.class);

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public Subject storeSubject(SubjectRequest request) throws ResourceAlreadyExistsException {
        try {
            Subject subject = Subject.from(request);
            Subject persistedSubject = repository.save(subject);
            LOGGER.info(String.format("Subject with id %s was created.", persistedSubject.getId()));
            return persistedSubject;
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Subject");
        }
    }
}
