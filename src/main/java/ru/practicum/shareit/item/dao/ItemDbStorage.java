package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Item createItem(Item item) {
        try {
            UserDto user = userService.getUser(item.getOwner());
            if (user == null) {
                throw new ObjectNotFoundException();
            }
            item.setId(generatedId());
            items.put(item.getId(), item);
            return item;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (items.containsKey(item.getId())) {
            items.put(item.getId(), item);
        } else throw new ObjectNotFoundException();
        return item;

    }

    @Override
    public Item getItem(Long id) {
        if (!items.containsKey(id)) {
            log.error("item does not exist");
        }
        return items.get(id);
    }

    @Override
    public List<Item> getAllItem() {
        return new ArrayList<>(items.values());
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
}
