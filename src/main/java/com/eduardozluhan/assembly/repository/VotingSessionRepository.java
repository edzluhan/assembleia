package com.eduardozluhan.assembly.repository;

import com.eduardozluhan.assembly.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
}
