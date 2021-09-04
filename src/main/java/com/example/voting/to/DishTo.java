package com.example.voting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo{
    double price;

    public DishTo(Integer id, String name, double price) {
        super(id, name);
        this.price = price;
    }
}
