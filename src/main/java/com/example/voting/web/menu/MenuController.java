package com.example.voting.web.menu;


import com.example.voting.model.Dish;
import com.example.voting.repository.DishRepository;
import com.example.voting.to.MenuTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.voting.util.VoteUtil.getVoteDate;
import static java.util.stream.Collectors.groupingBy;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {
    static final String REST_URL = "/api/menus";

    @Autowired
    protected DishRepository dishRepository;

    @GetMapping
    public List<MenuTo> getAll() {
        log.info("getAll");
        List<MenuTo> result = new ArrayList<>();

        dishRepository.findByRegistered(getVoteDate()).stream()
                .collect(groupingBy(Dish::getRestaurant))
                .forEach(((restaurant, dishes) -> result.add(new MenuTo(restaurant, dishes))));

        return result;
    }
}
