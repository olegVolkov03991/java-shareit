package ru.practicum.shareit.item.services;

import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Integer id);

    ItemDto updateItem(ItemDto itemDto, Integer id, Integer userId);

    Collection<Item> searchItems(String text);

    Optional<ItemFullDto> getItem(Integer id, Integer userId);

    Collection<ItemFullDto> findUserItems(Integer userId);

    Optional<CommentsDto> addItemComment(Integer itemId, Integer userId, CommentsDto commentDto);
}
