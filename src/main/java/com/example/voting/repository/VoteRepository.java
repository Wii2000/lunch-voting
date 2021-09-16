package com.example.voting.repository;

import com.example.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT v FROM Vote v WHERE v.registered=:date AND v.user.id=:userId")
    Optional<Vote> findByRegisteredAndUserId(LocalDate date, int userId);

    @Query("SELECT v FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    Optional<Vote> findByUserIdAndId(int userId, int id);

    @Query("SELECT v FROM Vote v WHERE v.registered=:date AND v.user.id=:userId AND v.id=:id")
    Vote getByRegisteredAndUserIdAndId(LocalDate date, int userId, int id);
}
