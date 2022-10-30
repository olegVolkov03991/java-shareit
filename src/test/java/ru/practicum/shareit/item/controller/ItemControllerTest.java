package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {

    private final EntityManager entityManager;
    private final ItemService itemService;
    private final UserService userService;

 UserDto userDto1 = UserDto.builder()
            .email("qwe@mail.ru")
            .name("qwe")
            .build();

     ItemDto itemDto1 = ItemDto.builder()
            .name("qwe")
            .description("qwe")
            .available(true)
            .owner(1)
            .build();




    @Test
    void createItem() {

        UserDto userDto = userService.createUser(userDto1);
        ItemDto itemDto = itemService.createItem(itemDto1, userDto.getId());

        TypedQuery<Item> query = entityManager.createQuery(
                "select  i from Item i where i.id = : id", Item.class);

        Item item1 = query.setParameter("id", itemDto.getId()).getSingleResult();

        assertThat(item1.getId(), notNullValue());
        assertThat(item1.getName(), equalTo(itemDto.getName()));
        assertThat(item1.getDescription(), equalTo(itemDto.getDescription()));

    }

}