package ru.practicum.shareit.item.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemDbStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public class ItemServices {

    private final ItemDbStorage itemDbStorage;

    @Autowired
    public ItemServices(ItemDbStorage itemDbStorage) {
        this.itemDbStorage = itemDbStorage;
    }

    public Item createItem(Item item, Long id) throws ObjectNotFoundException {
        return itemDbStorage.createItem(item, id);
    }

    public Item updateItem(Item item, Long id, Long userId) {
        return itemDbStorage.updateItem(item, id, userId);
    }

    public Item getItem(Long id) {
        return itemDbStorage.getItem(id);
    }

    public List<Item> getAllItems(Long userId) {
        return itemDbStorage.getAllItem(userId);
    }

    public List<Item> searchItem(String text, Long userId) {
        return itemDbStorage.searchItem(text, userId);
    }
}
