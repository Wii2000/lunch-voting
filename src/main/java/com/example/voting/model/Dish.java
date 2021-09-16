package com.example.voting.model;

import com.example.voting.Persist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"registered", "restaurant_id", "name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Dish extends NamedEntity {

    @Column(name = "price_in_cents", nullable = false)
    @Range(min = 10)
    @NotNull
    private Integer priceInCents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = Persist.class)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "registered", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate registered;

    public Dish(Integer id, String name, Integer priceInCents) {
        this(id, name, priceInCents, LocalDate.now());
    }

    public Dish(Integer id, String name, Integer priceInCents, LocalDate registered) {
        super(id, name);
        this.priceInCents = priceInCents;
        this.registered = registered;
    }
}
