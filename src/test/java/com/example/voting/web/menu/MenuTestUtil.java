package com.example.voting.web.menu;

import com.example.voting.to.MenuTo;
import com.example.voting.web.Matcher;

import java.util.List;

import static com.example.voting.web.dish.DishTestUtil.dishTos;
import static com.example.voting.web.restaurant.RestaurantTestUtil.restaurant1;

public class MenuTestUtil {
    public static final Matcher<MenuTo> MATCHER = new Matcher<>(MenuTo.class);

    public static final List<MenuTo> menus = List.of(new MenuTo(restaurant1, dishTos));

}
