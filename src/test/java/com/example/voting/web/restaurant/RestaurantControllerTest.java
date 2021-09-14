package com.example.voting.web.restaurant;

import com.example.voting.model.Restaurant;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting.web.restaurant.RestaurantController.REST_URL;
import static com.example.voting.web.restaurant.RestaurantTestUtil.*;
import static com.example.voting.web.user.UserTestUtil.ADMIN_MAIL;
import static com.example.voting.web.user.UserTestUtil.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = ADMIN_MAIL)
class RestaurantControllerTest extends AbstractControllerTest {
    static final String URL = REST_URL + "/";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurants));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + RESTAURANT_1_ID))
                .andExpect(status().isNoContent());
        assertThat(restaurantRepository.findById(RESTAURANT_1_ID)).isNotPresent();

    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "newRestaurant");
        ResultActions actions = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = JsonUtil.readValue(Matcher.getString(actions), Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(restaurantRepository.getById(newId), newRestaurant);
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(null, "updatedRestaurant");
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        Restaurant restaurant = restaurantRepository.getById(RESTAURANT_1_ID);
        updated.setId(restaurant.id());
        MATCHER.assertMatch(restaurant, updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant updated = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }
}