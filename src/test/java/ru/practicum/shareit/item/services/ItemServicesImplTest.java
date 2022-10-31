package ru.practicum.shareit.item.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class ItemServicesImplTest {

    UserDto userDto = UserDto.builder()
            .id(1)
            .email("qwe@mail.ru")
            .name("qwe")
            .build();

    ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("Qwe")
            .description("qwe")
            .available(true)
            .owner(1)
            .build();

    @InjectMocks
    private ItemServicesImpl itemServices;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void createItem() {

        User user = UserMapper.toUser(userDto);
        Item item = ItemMapper.toItem(itemDto, 1);

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(any())).thenReturn(item);

        Assertions.assertEquals(itemServices.createItem(itemDto, 1), itemDto);
    }
}