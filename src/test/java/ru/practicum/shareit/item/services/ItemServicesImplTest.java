package ru.practicum.shareit.item.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
class ItemServicesImplTest {

    private final Item item = new Item();
    private final User user = new User();

    @InjectMocks
    private ItemServicesImpl itemServices;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void createItem() {
        item.setId(1);
        item.setName("qwe");
        item.setDescription("qweqwe");
        item.setAvailable(true);
        item.setOwner(1);
        item.setRequestId(1);

        user.setId(1);
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(any())).thenReturn(item);

        Assertions.assertEquals(itemServices.createItem(ItemMapper.toItemDto(item), 1), ItemMapper.toItemDto(item));
    }

    @Test
    void searchItems() {
    }

    @Test
    void getItem() {
    }

    @Test
    void findUserItems() {
    }

    @Test
    void addItemComment() {
    }
}