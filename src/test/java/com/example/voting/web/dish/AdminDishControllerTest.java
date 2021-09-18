package com.example.voting.web.dish;

import com.example.voting.model.Dish;
import com.example.voting.repository.DishRepository;
import com.example.voting.util.JsonUtil;
import com.example.voting.web.AbstractControllerTest;
import com.example.voting.web.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting.web.dish.DishTestUtil.*;
import static com.example.voting.web.restaurant.RestaurantTestUtil.RESTAURANT_1_ID;
import static com.example.voting.web.user.UserTestUtil.ADMIN_MAIL;
import static com.example.voting.web.user.UserTestUtil.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = ADMIN_MAIL)
class AdminDishControllerTest extends AbstractControllerTest {
    private final static String URL = "/api/admin/restaurants/" + RESTAURANT_1_ID + "/dishes/";

    @Autowired
    DishRepository dishRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dishes));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH_2_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dish2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + DISH_2_ID))
                .andExpect(status().isNoContent());
        assertThat(dishRepository.findById(DISH_2_ID)).isNotPresent();
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + DISH_NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        Dish newDish = getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated())
                .andDo(print());

        Dish created = JsonUtil.readValue(Matcher.getString(actions), Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        MATCHER.assertMatch(created, newDish);
        MATCHER.assertMatch(dishRepository.getById(newId), newDish);
    }

    @Test
    void createInvalid() throws Exception {
        Dish newDish = new Dish(null, "d", 1);
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        Dish dish = getUpdated();
        perform(MockMvcRequestBuilders.put(URL + DISH_2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(dish)))
                .andExpect(status().isNoContent())
                .andDo(print());

        Dish updated = dishRepository.getById(DISH_2_ID);
        dish.setId(updated.id());
        MATCHER.assertMatch(updated, dish);
    }

    @Test
    void updateInvalid() throws Exception {
        Dish dish = new Dish(null, null, null);
        perform(MockMvcRequestBuilders.put(URL + DISH_2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(dish)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }
}