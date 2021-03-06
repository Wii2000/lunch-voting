package com.example.voting.web.dish;

import com.example.voting.model.Dish;
import com.example.voting.repository.DishRepository;
import com.example.voting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.example.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = {"menus"})
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurant_id}/dishes";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    @Operation(summary = "Get all dishes of restaurant")
    public List<Dish> getAll(@PathVariable("restaurant_id") int restaurantId) {
        log.info("get {}", restaurantId);
        return dishRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping(value = "/{dish_id}")
    @Operation(summary = "Get dish of restaurant by id")
    public ResponseEntity<Dish> get(
            @PathVariable("restaurant_id") int restaurantId, @PathVariable("dish_id") int id
    ) {
        log.info("get {} for restaurantID {}", id, restaurantId);
        return ResponseEntity.of(dishRepository.findByRestaurantIdAndId(restaurantId, id));
    }

    @DeleteMapping(value = "/{dish_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Delete dish of restaurant by id")
    public void delete(
            @PathVariable("restaurant_id") int restaurantId, @PathVariable("dish_id") int id
    ) {
        log.info("delete {} for restaurantID {}", id, restaurantId);
        checkModification(dishRepository.delete(restaurantId, id), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "Create dish of restaurant")
    public ResponseEntity<Dish> createWithLocation(
            @PathVariable("restaurant_id") int restaurantId, @Valid @RequestBody Dish dish
    ) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{dish_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "Update dish of restaurant")
    public void update(
            @PathVariable("restaurant_id") int restaurantId,
            @PathVariable("dish_id") int dishId,
            @Valid @RequestBody Dish dish
    ) {
        log.info("update {} with id={}", dish, dishId);
        assureIdConsistent(dish, dishId);
        checkNotFoundWithId(dishRepository.findByRestaurantIdAndId(restaurantId, dishId)
                .orElse(null), dishId);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        dishRepository.save(dish);
    }
}

