package com.eduardozluhan.assembly.repository;

import com.eduardozluhan.assembly.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
