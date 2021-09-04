package com.example.voting.web.dish;

import com.example.voting.to.DishTo;
import com.example.voting.web.Matcher;
import com.example.voting.model.Dish;

import java.util.List;

public class DishTestUtil {
    public static final Matcher<Dish> MATCHER = new Matcher<>(Dish.class, "restaurant");
    public static final int DISH_1_ID = 1;
    public static final int DISH_2_ID = 2;
    public static final int DISH_3_ID = 3;
    public static final int DISH_NOT_FOUND = 100;

    public static final Dish dish1 = new Dish(DISH_1_ID, "Dish1", 375);
    public static final Dish dish2 = new Dish(DISH_2_ID, "Dish2", 450);
    public static final Dish dish3 = new Dish(DISH_3_ID, "Dish3", 350);

    public static final List<Dish> dishes = List.of(dish1, dish2, dish3);

    public static final DishTo dishTo1 = new DishTo(dish1.id(), dish1.getName(), dish1.getPriceInCents());
    public static final DishTo dishTo2 = new DishTo(dish2.id(), dish2.getName(), dish2.getPriceInCents());
    public static final DishTo dishTo3 = new DishTo(dish3.id(), dish3.getName(), dish3.getPriceInCents());

    public static final List<DishTo> dishTos = List.of(dishTo1, dishTo2, dishTo3);

    public static Dish getNew() {
        return new Dish(null, "newDish", 100);
    }
    public static Dish getUpdated() {
        return new Dish(null, "updatedDish", 600);
    }

}
