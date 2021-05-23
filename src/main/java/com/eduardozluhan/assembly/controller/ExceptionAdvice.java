package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.exceptions.ResourceAlreadyExistsException;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(VotingSessionNotAvailableException.class)
    public ResponseEntity<String> notFound(VotingSessionNotAvailableException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyVotedException.class, ResourceAlreadyExistsException.class})
    public ResponseEntity<String> conflict(UserAlreadyVotedException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
