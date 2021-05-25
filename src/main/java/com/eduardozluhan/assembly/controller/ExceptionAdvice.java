package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.exceptions.InvalidVoteValueException;
import com.eduardozluhan.assembly.exceptions.ResourceAlreadyExistsException;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(VotingSessionNotAvailableException.class)
    public ResponseEntity<String> notFound(VotingSessionNotAvailableException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyVotedException.class)
    public ResponseEntity<String> conflict(UserAlreadyVotedException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> conflict(ResourceAlreadyExistsException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(InvalidVoteValueException.class)
    public ResponseEntity<String> badRequest(InvalidVoteValueException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> badRequest(ConstraintViolationException e) {
        List<String> errorMessages = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.toList());

        LOGGER.error(errorMessages.toString());
        return new ResponseEntity<>(errorMessages.toString(), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> badRequest(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }
}
