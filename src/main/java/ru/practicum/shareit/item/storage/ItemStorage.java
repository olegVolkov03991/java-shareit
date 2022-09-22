package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.io.IOException;
import java.util.List;

public interface ItemStorage {
    Object createItem(Item item) throws ObjectNotFoundException, IOException;

    Item updateItem(Item item);

    Item getItem(Long id);

    List<Item> getAllItem();

    List<Item> searchItem(String text, Long userId);
}
