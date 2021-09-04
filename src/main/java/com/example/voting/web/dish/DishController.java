package com.example.voting.web.dish;

import com.example.voting.Web;
import com.example.voting.model.Dish;
import com.example.voting.repository.DishRepository;
import com.example.voting.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurant_id}/dishes";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Dish> getAll(@PathVariable("restaurant_id") int restaurantId) {
        log.info("get {}", restaurantId);
        return dishRepository.findByRestaurant(restaurantId);
    }

    @GetMapping(value = "/{dish_id}")
    public ResponseEntity<Dish> get(
            @PathVariable("restaurant_id") int restaurantId, @PathVariable("dish_id") int id
    ) {
        log.info("get {} for restaurantID {}", id, restaurantId);
        return ResponseEntity.of(dishRepository.findByRestaurantAndId(restaurantId, id));
    }

    @DeleteMapping(value = "/{dish_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable("restaurant_id") int restaurantId, @PathVariable("dish_id") int id
    ) {
        log.info("delete {} for restaurantID {}", id, restaurantId);
        checkModification(dishRepository.delete(restaurantId, id), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(
            @PathVariable("restaurant_id") int restaurantId, @Validated(Web.class) @RequestBody Dish dish
    ) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(checkNotFoundWithId(
                restaurantRepository.findById(restaurantId).orElse(null), restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{dish_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable("restaurant_id") int restaurantId,
            @PathVariable("dish_id") int dishId,
            @Validated(Web.class) @RequestBody Dish dish
    ) {
        log.info("update {} with id={}", dish, dishId);
        assureIdConsistent(dish, dishId);
        Dish dbDish = checkNotFoundWithId(dishRepository.findById(dishId)
                .filter(d -> d.getRestaurant().getId().equals(restaurantId)).orElse(null), dishId);
        dish.setRestaurant(dbDish.getRestaurant());
        dishRepository.save(dish);
    }
}

