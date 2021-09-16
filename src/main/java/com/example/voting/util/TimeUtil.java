package com.example.voting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class TimeUtil {
    public static final LocalTime VOTING_CHANGE_END_TIME = LocalTime.of(11, 0);
    public static final LocalTime VOTE_TIME_IS_UP = VOTING_CHANGE_END_TIME.plusHours(1);
    public static final LocalTime VOTE_IN_TIME = VOTING_CHANGE_END_TIME.minusHours(1);
}
