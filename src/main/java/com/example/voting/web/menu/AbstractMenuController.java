package com.example.voting.web.menu;

import com.example.voting.model.BaseEntity;
import com.example.voting.model.Restaurant;
import com.example.voting.repository.DishRepository;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.to.MenuTo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class AbstractMenuController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    protected List<MenuTo> getByDate(LocalDate date) {
        List<MenuTo> result = new ArrayList<>();

        Map<Integer, Restaurant> restaurants = restaurantRepository.findAll().stream().collect(toMap(
                BaseEntity::getId,
                Function.identity()
        ));

        dishRepository.findByRegistered(date).stream()
                .collect(groupingBy(dish -> dish.getRestaurant().getId()))
                .forEach(((restaurantId, dishes) ->
                        result.add(new MenuTo(restaurants.get(restaurantId), dishes))));

        return result;
    }
}
