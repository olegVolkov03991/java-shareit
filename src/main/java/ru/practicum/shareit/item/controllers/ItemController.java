package ru.practicum.shareit.item.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.services.ItemServices;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServices itemServices;

    @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long id) throws ObjectNotFoundException {

        return itemServices.createItem(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto item, @PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.updateItem(item, id, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Long id) {
        return itemServices.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServices.searchItem(text);
    }
}
