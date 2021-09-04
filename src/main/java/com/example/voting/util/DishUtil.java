package com.example.voting.util;

import com.example.voting.model.Dish;
import com.example.voting.to.DishTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class DishUtil {
    private static final int DIVIDER_FROM_TO_DOLLARS = 100;

    public static List<DishTo> getTo(Collection<Dish> dishes) {
        return dishes.stream().map(DishUtil::getTo).toList();
    }

    public static DishTo getTo(Dish dish) {
        return new DishTo(dish.id(), dish.getName(), (double)dish.getPriceInCents()/ DIVIDER_FROM_TO_DOLLARS);
    }
}
