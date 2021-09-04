package com.example.voting.to;

import com.example.voting.model.Dish;
import com.example.voting.model.Restaurant;
import lombok.Value;

import java.util.List;

@Value
public class MenuTo {
    Restaurant restaurant;
    List<Dish> dishes;

    public MenuTo(Restaurant restaurant, List<Dish> dishes) {
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
