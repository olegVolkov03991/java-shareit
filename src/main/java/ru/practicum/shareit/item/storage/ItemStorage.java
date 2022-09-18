package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.io.IOException;
import java.util.List;

public interface ItemStorage {
    Object createItem(Item item, Long id) throws ObjectNotFoundException, IOException;

    Item updateItem(Item item, Long id, Long userId);

    Item getItem(Long id);

    List<Item> getAllItem(Long userId);

    List<Item> searchItem(String text, Long userId);
}
