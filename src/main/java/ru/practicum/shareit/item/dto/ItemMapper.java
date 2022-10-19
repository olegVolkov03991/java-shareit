package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto, Integer userId) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(userId);
        item.setRequest(itemDto.getRequest());
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    public static ItemFullDto toItemFullDto(Item item,
                                            Optional<Booking> lastBooking,
                                            Optional<Booking> nextBooking,
                                            Collection<CommentsDto> comments) {
        ItemFullDto toItemFullDto = new ItemFullDto();
        if (item != null) {
            toItemFullDto.setId(item.getId());
            toItemFullDto.setName(item.getName());
            toItemFullDto.setDescription(item.getDescription());
            toItemFullDto.setAvailable(item.getAvailable());
            if (lastBooking.isPresent()) {
                toItemFullDto.setLastBooking(lastBooking.get());
            }
            if (nextBooking.isPresent()) {
                toItemFullDto.setNextBooking(nextBooking.get());
            }
            if (comments != null && comments.size() > 0) {
                toItemFullDto.setComments(comments);
            } else {
                toItemFullDto.setComments(new ArrayList<>());
            }
        }
        return toItemFullDto;
    }
}
