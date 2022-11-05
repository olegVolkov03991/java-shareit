package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") int id) {
        log.info("create item{}, user{}", itemDto, id);
        return itemClient.create(id, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto item, @PathVariable int itemId,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("update item{}, itemId{}, user{}", item, itemId, userId);
        return itemClient.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable("itemId") int itemId) {
        log.info("Get item{}, user{}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "text", required = false) String text,
                                              @PositiveOrZero
                                              @RequestParam(value = "from", defaultValue = "0") int from,
                                              @Positive
                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get items with text={}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable int id,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("add comment id{}, comment{}, user{}", id, commentDto, userId);
        return itemClient.createComment(userId, id, commentDto);
    }
}
