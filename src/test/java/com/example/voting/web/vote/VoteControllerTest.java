package com.example.voting.web.vote;

import com.example.voting.model.Vote;
import com.example.voting.repository.VoteRepository;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting.util.VoteUtil.getVoteDate;
import static com.example.voting.web.restaurant.RestaurantTestUtil.*;
import static com.example.voting.web.user.UserTestUtil.USER_MAIL;
import static com.example.voting.web.user.UserTestUtil.user;
import static com.example.voting.web.vote.VoteController.REST_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    static final String URL = REST_URL + "?restaurantId=";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void create() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.post(URL + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = JsonUtil.readValue(Matcher.getString(actions), Vote.class);
        assertThat(created.getRestaurant().getId()).isEqualTo(RESTAURANT_1_ID);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        voteRepository.save(new Vote(null, user, restaurant1, getVoteDate()));

        ResultActions actions = perform(MockMvcRequestBuilders.post(URL + RESTAURANT_2_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        Vote created = JsonUtil.readValue(Matcher.getString(actions), Vote.class);
        assertThat(created.getRestaurant().getId()).isEqualTo(RESTAURANT_2_ID);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(URL + RESTAURANT_NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }
}