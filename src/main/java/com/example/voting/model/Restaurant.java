package com.example.voting.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {
    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
