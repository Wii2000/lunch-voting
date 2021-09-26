package com.example.voting.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Dish> dishes;
}
