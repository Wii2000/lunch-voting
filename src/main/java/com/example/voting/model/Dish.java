package com.example.voting.model;

import com.example.voting.HasId;
import com.example.voting.Web;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity implements HasId {

    @Column(name = "price_in_cents", nullable = false)
    @Range(min = 10, max = 5000, groups = Web.class)
    @NotNull(groups = Web.class)
    private Integer priceInCents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @NotNull
    private Restaurant restaurant;

    @Column(name = "registered", nullable = false, columnDefinition = "date default now()")
    @NotNull(groups = Web.class)
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
