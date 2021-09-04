package com.example.voting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class VoteUtil {
    public static final LocalTime VOTING_END_TIME = LocalTime.of(11, 0);

    public static LocalDate getVoteDate() {
        return LocalTime.now().isBefore(VOTING_END_TIME) ? LocalDate.now() : LocalDate.now().plusDays(1);
    }
}
