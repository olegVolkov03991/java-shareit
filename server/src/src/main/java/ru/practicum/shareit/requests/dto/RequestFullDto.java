package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
public class RequestFullDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private Collection<Item> items;
}
