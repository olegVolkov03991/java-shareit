package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("create request{}, user{}", requestDto, id);
        return requestClient.create(id, requestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        log.info("Get request{}, user{}", requestId, userId);
        return requestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(required = false, defaultValue = "20")
                                                 @PositiveOrZero int size) {
        log.info("get all requests user{}, from{}, size{}", userId, from, size);
        return requestClient.getAll(userId, from, size);
    }
}
