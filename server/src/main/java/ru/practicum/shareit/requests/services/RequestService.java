package ru.practicum.shareit.requests.services;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;

import java.util.Collection;
import java.util.List;

public interface RequestService {
    RequestDto create(ItemDto itemDto, Integer userId);

    RequestFullDto getById(Integer id, Integer userId);

    Collection<RequestFullDto> getRequestsAll(Integer userId);

    List<RequestFullDto> getAll(Integer userId, Integer from, Integer size);
}
