package ru.practicum.shareit.requests.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.services.RequestServiceImpl;

import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestContoller {
    private final RequestServiceImpl requestService;

    @PostMapping
    public RequestDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        return requestService.create(itemDto, id);
    }

    @GetMapping("/{id}")
    public RequestFullDto getById(@PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getById(id, userId);
    }

    @GetMapping
    public Collection<RequestFullDto> getRequestsAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getRequestsAll(userId);
    }

    @GetMapping("/all")
    public List<RequestFullDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam(required = false, defaultValue = "0")
                                               @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "20")
                                               @PositiveOrZero Integer size) {
        return requestService.getAll(userId, from, size);
    }

}
