package ru.job4j.todo.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String description;

    private LocalDateTime created = LocalDateTime.now();

    private boolean done;

    public Task() {

    }
}
