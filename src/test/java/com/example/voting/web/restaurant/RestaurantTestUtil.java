package com.example.voting.web.restaurant;

import com.example.voting.web.Matcher;
import com.example.voting.model.Restaurant;

import java.util.List;

public class RestaurantTestUtil {
    public static final Matcher<Restaurant> MATCHER = new Matcher<>(Restaurant.class);
    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final int RESTAURANT_3_ID = 3;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, "Restaurant2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, "Restaurant3");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3);
}
