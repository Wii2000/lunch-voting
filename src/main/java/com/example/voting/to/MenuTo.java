package com.example.voting.to;

import com.example.voting.model.Restaurant;
import lombok.Value;

import java.util.List;

@Value
public class MenuTo {
    Restaurant restaurant;
    List<DishTo> dishes;

    public MenuTo(Restaurant restaurant, List<DishTo> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
