package ru.practicum.shareit.requests.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.services.RequestServiceImpl;

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

    @GetMapping("/{requestId}")
    public RequestFullDto getById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer requestId) {
        return requestService.getById(requestId, userId);
    }

    @GetMapping
    public Collection<RequestFullDto> getRequestsAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getRequestsAll(userId);
    }

    @GetMapping("/all")
    public List<RequestFullDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                               @RequestParam Integer from,
                                               @RequestParam Integer size) {
        return requestService.getAll(userId, from, size);
    }

}
