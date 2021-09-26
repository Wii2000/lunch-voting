package com.example.voting.web.restaurant;

import com.example.voting.model.Restaurant;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting.web.Matcher.getContent;
import static com.example.voting.web.restaurant.RestaurantController.REST_URL;
import static com.example.voting.web.user.UserTestUtil.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(JsonUtil.readValues(getContent(result), Restaurant.class)).hasSize(3))
                .andDo(print());
    }
}
