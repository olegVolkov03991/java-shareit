package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemServices;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServices itemServices;

    @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }

    @PostMapping
    public Item createItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long id) throws ObjectNotFoundException {

        return itemServices.createItem(item, id);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestBody Item item, @PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.updateItem(item, id, userId);
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id) {
        return itemServices.getItem(id);
    }

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.searchItem(text, userId);
    }
}
