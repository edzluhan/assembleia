package com.eduardozluhan.assembly.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "voting_sessions")
public class VotingSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "subject_id")
    private Integer subjectId;

    @NotNull
    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    public VotingSession(@NotNull Integer subjectId, @NotNull LocalDateTime endsAt) {
        this.subjectId = subjectId;
        this.endsAt = endsAt;
    }
}
