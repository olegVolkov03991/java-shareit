package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemServicesImpl;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServicesImpl itemServicesImpl;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Integer id) throws ObjectNotFoundException {

        return itemServicesImpl.createItem(item, id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto item, @PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemServicesImpl.updateItem(item, id, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemFullDto> findItemById(@PathVariable Integer id,
                                                    @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemServicesImpl.getItem(id, userId).map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Collection<ItemFullDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemServicesImpl.findUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchItems(@RequestParam(required = false) String text) {
        return itemServicesImpl.searchItems(text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentsDto> addItemComment(@PathVariable Integer id,
                                                      @Valid @RequestBody CommentsDto commentDto,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemServicesImpl.addItemComment(id, userId, commentDto).map(comment -> new ResponseEntity<>(comment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
