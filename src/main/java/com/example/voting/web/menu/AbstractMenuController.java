package com.example.voting.web.menu;

import com.example.voting.repository.DishRepository;
import com.example.voting.repository.RestaurantRepository;
import com.example.voting.to.MenuTo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class AbstractMenuController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    protected List<MenuTo> getByDate(LocalDate date) {
        List<MenuTo> result = new ArrayList<>();

        dishRepository.findByRegistered(date).stream()
                .collect(groupingBy(dish -> dish.getRestaurant().getId()))
                .forEach(((restaurantId, dishes) ->
                        result.add(new MenuTo(restaurantRepository.findById(restaurantId)
                                .orElse(null), dishes))));

        return result;
    }
}
