package com.example.voting.web.restaurant;

import com.example.voting.web.Matcher;
import com.example.voting.model.Restaurant;

public class RestaurantTestUtil {
    public static final Matcher<Restaurant> MATCHER = new Matcher<>(Restaurant.class);
    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final int RESTAURANT_NOT_FOUND = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "Restaurant1");
}
