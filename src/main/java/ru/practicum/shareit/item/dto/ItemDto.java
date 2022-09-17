package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
public class ItemDto {
    Long id;
    String name;
    String description;
    boolean available;
    User owner;
    Long request;

    public ItemDto(String name, String description, boolean available) {
    }
}
