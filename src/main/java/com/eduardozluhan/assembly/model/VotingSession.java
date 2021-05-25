package com.eduardozluhan.assembly.model;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "voting_sessions")
public class VotingSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subject_id")
    private Long subjectId;

    @NotNull
    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    public VotingSession(Long subjectId, LocalDateTime endsAt) {
        this.subjectId = subjectId;
        this.endsAt = endsAt;
    }
}
