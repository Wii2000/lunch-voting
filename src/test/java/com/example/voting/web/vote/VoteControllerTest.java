package com.example.voting.web.vote;

import com.example.voting.model.Vote;
import com.example.voting.repository.VoteRepository;
import com.example.voting.util.JsonUtil;
import com.example.voting.util.TimeUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.GlobalExceptionHandler;
import com.example.voting.web.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static com.example.voting.web.GlobalExceptionHandler.EXCEPTION_CHANGE_VOTE;
import static com.example.voting.web.restaurant.RestaurantTestUtil.RESTAURANT_1_ID;
import static com.example.voting.web.restaurant.RestaurantTestUtil.RESTAURANT_2_ID;
import static com.example.voting.web.user.UserTestUtil.*;
import static com.example.voting.web.vote.VoteController.REST_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    static final String URL = REST_URL;
    private static final int USER_2_VOTE_ID = 1;

    @Autowired
    private VoteRepository voteRepository;

    @MockBean
    private Clock clock;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void create() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.post(URL + "?restaurantId=" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = JsonUtil.readValue(Matcher.getString(actions), Vote.class);
        int newId = created.id();
        created = voteRepository.getById(newId);
        assertThat(created.getRestaurant().getId()).isEqualTo(RESTAURANT_1_ID);
        assertThat(created.getUser().getId()).isEqualTo(USER_ID);
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(URL + "?restaurantId=" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE)));
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void updateInTime() throws Exception {
        setTimeOnClock(TimeUtil.VOTE_IN_TIME);

        perform(MockMvcRequestBuilders.put(URL + "/" + USER_2_VOTE_ID + "?restaurantId=" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertThat(voteRepository.getById(USER_2_VOTE_ID).getRestaurant().getId()).isEqualTo(RESTAURANT_1_ID);
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void updateAfterTimeIsUp() throws Exception {
        setTimeOnClock(TimeUtil.VOTE_TIME_IS_UP);

        perform(MockMvcRequestBuilders.put(URL + "/" + USER_2_VOTE_ID + "?restaurantId=" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_CHANGE_VOTE)))
                .andDo(print());

        assertThat(voteRepository.getById(USER_2_VOTE_ID).getRestaurant().getId()).isEqualTo(RESTAURANT_2_ID);
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(URL + "?restaurantId=" + RESTAURANT_1_ID))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    //    https://stackoverflow.com/a/32794740/16047333
    private void setTimeOnClock(LocalTime voteInTime) {
        Clock fixedClock = Clock.fixed(
                voteInTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }
}