package com.eduardozluhan.assembly.controller;

import com.eduardozluhan.assembly.controller.request.OpenVotingSessionRequest;
import com.eduardozluhan.assembly.exceptions.VotingSessionNotAvailableException;
import com.eduardozluhan.assembly.model.VotingSession;
import com.eduardozluhan.assembly.model.VotingSessionReport;
import com.eduardozluhan.assembly.service.VotingSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
public class VotingSessionController {
    private final VotingSessionService service;

    public VotingSessionController(VotingSessionService service) {
        this.service = service;
    }

    @PostMapping(path = "/voting-session")
    public ResponseEntity<VotingSession> openVotingSession(@RequestBody @Valid OpenVotingSessionRequest request) {
        return new ResponseEntity<>(
                service.openVotingSession(request.getSubjectId(), request.getDurationInMinutes()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/voting-session/{votingSessionId}/report")
    public ResponseEntity<VotingSessionReport> getVotingSessionReport(@PathVariable @NotNull Long votingSessionId)
            throws VotingSessionNotAvailableException {
        return new ResponseEntity<>(service.sessionReport(votingSessionId), HttpStatus.OK);
    }
}
