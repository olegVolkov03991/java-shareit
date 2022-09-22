package ru.practicum.shareit.item.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadReqestException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDbStorage;
import ru.practicum.shareit.user.dao.UserDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServices {

    private final ItemDbStorage itemDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public ItemServices(ItemDbStorage itemDbStorage, UserDbStorage userDbStorage) {
        this.itemDbStorage = itemDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public ItemDto createItem(ItemDto item, Long id) throws ObjectNotFoundException {
        if (itemValidation(item) && ownerValidation(id)) {
            return ItemMapper.toItemDto(itemDbStorage.createItem(ItemMapper.toItem(item, id)));
        }
        throw new ObjectNotFoundException();
    }

    public ItemDto updateItem(ItemDto item, Long id, Long userId) {
        if (ownerValidation(userId) && itemDbStorage.getItem(id).getOwner().equals(userId)) {
            return ItemMapper.toItemDto(itemDbStorage.updateItem(ItemMapper.updateItem(itemDbStorage.getItem(id), ItemMapper.toItem(item, userId))));
        } else throw new ObjectNotFoundException();
    }

    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(itemDbStorage.getItem(id));
    }

    public List<ItemDto> getAllItems(Long userId) {
        if (ownerValidation(userId)) {
            return ItemMapper.toItemsDto(itemDbStorage.getAllItem())
                    .stream()
                    .filter(itemDto -> itemDto.getOwner().equals(userId))
                    .collect(Collectors.toList());
        }
        throw new ObjectNotFoundException();
    }

    public List<ItemDto> searchItem(String text) {
        if (text != null && !text.isBlank()) {
            String textSearch = text.toLowerCase();
            return ItemMapper.toItemsDto(itemDbStorage.getAllItem())
                    .stream()
                    .filter(i -> i.getName().toLowerCase().contains(textSearch)
                            || i.getDescription().toLowerCase().contains(textSearch))
                    .filter(ItemDto::getAvailable)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private boolean itemValidation(ItemDto item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadReqestException();
        }
        if (item.getAvailable() == null) {
            throw new BadReqestException();
        }
        if (item.getDescription() == null) {
            throw new BadReqestException();
        }
        return true;
    }

    private boolean ownerValidation(Long userId) {
        return userDbStorage.getUser(userId) != null;
    }
}
