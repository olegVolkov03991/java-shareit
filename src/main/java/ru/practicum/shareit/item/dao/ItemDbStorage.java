package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import java.util.*;

@Component
@Slf4j
public class ItemDbStorage implements ItemStorage {
    private final UserService userService;
    private final Map<Long, Item> items = new HashMap<>();
    private long id;

    @Autowired
    public ItemDbStorage(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Item createItem(Item item, Long userId) {
        try {
            User user = userService.getUser(userId);
            if (user == null) {
                throw new ObjectNotFoundException();
            }
            Item newItem = new Item();
            boolean available = item.getAvailable();
            newItem.setName(item.getName());
            newItem.setDescription(item.getDescription());
            newItem.setAvailable(item.getAvailable());
            newItem.setId(generatedId());
            newItem.setOwner(user);
            newItem.setAvailable(available);
            if (!items.containsValue(newItem))
                items.put(newItem.getId(), newItem);
            return newItem;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        Item newItem = getItem(itemId);
        if (items.containsKey(itemId) && Objects.equals(newItem.getOwner().getId(), userId)) {
            fullUpdate(item, itemId, newItem);
            updateItemAvaildable(item, itemId, newItem, userId);
            updateItemDescription(item, itemId, newItem, userId);
            updateItemName(item, itemId, newItem, userId);
            return newItem;
        } else {
            throw new ObjectNotFoundException();
        }

    }

    @Override
    public Item getItem(Long id) {
        if (!items.containsValue(id)) {
            log.error("item does not exist");
        }
        return items.get(id);
    }

    @Override
    public List<Item> getAllItem(Long userId) {
        List<Item> items1 = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId().equals(userId)) {
                items1.add(item);
                return items1;
            }
        }
        return items1;
    }

    @Override
    public List<Item> searchItem(String text, Long userId) {
        List<Item> listItems = new ArrayList<>();
        if (text.length() == 0)
            return listItems;
        String text1 = text.toLowerCase();
        for (Item item1 : items.values()) {
            String name = item1.getName().toLowerCase();
            String description = item1.getDescription().toLowerCase();
            if (name.contains(text1) || description.contains(text1) && item1.getAvailable()) {
                if (!listItems.contains(item1)) {
                    listItems.add(item1);
                }
            }
        }
        return listItems;
    }

    public Long generatedId() {
        return ++id;
    }

    public void fullUpdate(Item item, Long itemId, Item newItem) {
        if (item.getName() != null && item.getDescription() != null && item.getAvailable() != null) {
            newItem.setId(itemId);
            newItem.setName(item.getName());
            newItem.setDescription(item.getDescription());
            newItem.setAvailable(item.getAvailable());
            items.put(newItem.getId(), newItem);
        }
    }

    public void updateItemAvaildable(Item item, Long itemId, Item newItem, Long userId) {
        if (item.getName() == null && item.getDescription() == null && item.getAvailable() != null) {
            newItem.setId(itemId);
            newItem.setName(getItem(itemId).getName());
            newItem.setDescription(getItem(itemId).getDescription());
            newItem.setAvailable(item.getAvailable());
            items.put(userId, newItem);
        }
    }

    public void updateItemDescription(Item item, Long itemId, Item newItem, Long userId) {
        if (item.getName() == null && item.getDescription() != null) {
            newItem.setId(itemId);
            newItem.setName(newItem.getName());
            newItem.setDescription(item.getDescription());
            newItem.setAvailable(newItem.getAvailable());
            items.put(userId, newItem);
        }

    }

    public void updateItemName(Item item, Long itemId, Item newItem, Long userId) {
        if (item.getName() != null && item.getDescription() == null) {
            newItem.setId(itemId);
            newItem.setName(item.getName());
            newItem.setDescription(newItem.getDescription());
            newItem.setAvailable(newItem.getAvailable());
            items.put(userId, newItem);
        }
    }
}
