package com.example.voting.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_id", "registered"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "registered", nullable = false)
    @NotNull
    private LocalDate registered;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate registered) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.registered = registered;
    }
}
