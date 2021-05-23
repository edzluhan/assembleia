package com.eduardozluhan.assembly.model;

import com.eduardozluhan.assembly.controller.request.VoteRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "voting_session_id")
    private Long votingSessionId;

    @NotNull
    @Column(name = "vote_value")
    private String voteValue;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;

    public Vote(String userId, Long votingSessionId, String voteValue, LocalDateTime votedAt) {
        this.userId = userId;
        this.votingSessionId = votingSessionId;
        this.voteValue = voteValue;
        this.votedAt = LocalDateTime.now();
    }

    public static Vote from(VoteRequest request) {
        return new Vote(request.getUserId(), request.getVotingSessionId(), request.getVoteValue(), LocalDateTime.now());
    }
}
