package ru.practicum.shareit.requests.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.Request;

import java.util.Collection;


@Component
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated()
        );
    }

    public static RequestFullDto toRequestFullDto(RequestDto requestDto, Collection<Item> items) {
        return new RequestFullDto(
                requestDto.getId(),
                requestDto.getDescription(),
                requestDto.getCreated(),
                items

        );
    }
}
