package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotNull
    private String description;
    @NotNull
    @Column(nullable = false)
    private Boolean available;
    private Integer owner;
    private Integer request;
}