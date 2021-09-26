package com.example.voting.web.restaurant;

import com.example.voting.model.Restaurant;
import com.example.voting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/menus";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    @Cacheable(cacheNames = "menus", sync = true)
    @Operation(summary = "Get restaurants menus for today")
    public List<Restaurant> getToday() {
        log.info("getMenusByToday");
        return restaurantRepository.getWithDishesByDate(LocalDate.now());
    }
}
