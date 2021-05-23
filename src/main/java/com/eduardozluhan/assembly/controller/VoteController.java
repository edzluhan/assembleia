package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.controller.request.VoteRequest;
import com.eduardozluhan.assembly.exceptions.UserAlreadyVotedException;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.Vote;
import com.eduardozluhan.assembly.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class VoteController {
    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Vote> registerVote(@RequestBody @Valid VoteRequest request) throws VotingSessionNotAvailableException, UserAlreadyVotedException {
        return new ResponseEntity<>(service.registerVote(request), HttpStatus.CREATED);
    }
}
