package com.example.voting.web.vote;

import com.example.voting.model.Vote;
import com.example.voting.repository.VoteRepository;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.GlobalExceptionHandler;
import com.example.voting.web.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.example.voting.web.restaurant.RestaurantTestUtil.RESTAURANT_1_ID;
import static com.example.voting.web.restaurant.RestaurantTestUtil.RESTAURANT_2_ID;
import static com.example.voting.web.user.UserTestUtil.*;
import static com.example.voting.web.vote.VoteController.REST_URL;
import static com.example.voting.web.vote.VoteController.VOTING_CHANGE_END_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    static final String URL = REST_URL;

    @Autowired
    private VoteRepository voteRepository;

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
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(URL + "/" + 1 + "?restaurantId=" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(LocalTime.now().isBefore(VOTING_CHANGE_END_TIME) ?
                        status().isCreated() : status().isUnprocessableEntity())
                .andDo(print());

        assertThat(voteRepository.getById(1).getRestaurant().getId())
                .isEqualTo(LocalTime.now().isBefore(VOTING_CHANGE_END_TIME) ? RESTAURANT_1_ID : RESTAURANT_2_ID);
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(URL + "?restaurantId=" + RESTAURANT_1_ID))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}