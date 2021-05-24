package com.eduardozluhan.assembly.repository;

import com.eduardozluhan.assembly.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    public static final String YES = "sim";
    public static final String NO = "não";

    @Query(value = "SELECT COUNT(*) FROM votes v WHERE LOWER(v.vote_value) = :value " +
            "AND v.voting_session_id = :votingSessionId", nativeQuery = true)
    Long countVotesByValueForSession(@Param("value") String value, @Param("votingSessionId") Long votingSessionId);
}
