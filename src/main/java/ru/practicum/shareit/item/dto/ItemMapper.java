package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto, Long owner) {
        if (itemDto == null) {
            return null;
        }
        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .name(itemDto.getName())
                .owner(owner)
                .request(itemDto.getRequest())
                .build();
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

    public static Item updateItem(Item item, Item updateItem) {
        item.setName(updateItem.getName() == null ? item.getName() : updateItem.getName());
        item.setDescription(updateItem.getDescription() == null ? item.getDescription() : updateItem.getDescription());
        item.setAvailable(updateItem.getAvailable() == null ? item.getAvailable() : updateItem.getAvailable());
        return item;
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
