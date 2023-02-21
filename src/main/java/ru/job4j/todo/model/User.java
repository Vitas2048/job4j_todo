package ru.job4j.todo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.TimeZone;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private String login;

    private String password;

    private TimeZone zone = TimeZone.getDefault();
}
