package com.example.voting.repository;

import com.example.voting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:dishId AND d.restaurant.id=:restaurantId")
    int delete(int restaurantId, int dishId);

    List<Dish> findByRegistered(LocalDate registered);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.registered DESC, d.name ASC")
    List<Dish> findByRestaurantId(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.id=:id")
    Optional<Dish> findByRestaurantIdAndId(int restaurantId, int id);
}

