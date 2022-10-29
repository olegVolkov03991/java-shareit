package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
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
import ru.practicum.shareit.user.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    User user = new User();
    Item item = new Item();


    @Test
    void createItem() {
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        UserDto userDto = userService.createUser(UserMapper.toUserDto(user));
        ItemDto itemDto = itemService.createItem(ItemMapper.toItemDto(item), userDto.getId());

        TypedQuery<Item> query = entityManager.createQuery(
                "select  i from Item i where i.id = : id", Item.class);

        Item item1 = query.setParameter("id", itemDto.getId()).getSingleResult();

        assertThat(item1.getId(), notNullValue());
        assertThat(item1.getName(), equalTo(itemDto.getName()));
        assertThat(item1.getDescription(), equalTo(itemDto.getDescription()));

    }

    @Test
    void findItemById() {
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        userService.createUser(UserMapper.toUserDto(user));
        itemService.createItem(ItemMapper.toItemDto(item), 1);
        Optional<ItemFullDto> itemFullDto = itemService.getItem(item.getId(), 1);

        assertThat(itemFullDto.get().getId(), notNullValue());
        assertThat(itemFullDto.get().getName(), equalTo(item.getName()));
        assertThat(itemFullDto.get().getDescription(), equalTo(item.getDescription()));
    }

    @Test
    void getAllItems() {

        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        User user1 = new User();
        user1.setId(2);
        user1.setName("wewef");
        user1.setEmail("svav@mail.ru");

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        Item item1 = new Item();

        item1.setAvailable(true);
        item1.setDescription("qw1eqwe");
        item1.setName("qw1e");
        item1.setId(2);
        item1.setOwner(2);

        userService.createUser(UserMapper.toUserDto(user));
        userService.createUser(UserMapper.toUserDto(user1));
        itemService.createItem(ItemMapper.toItemDto(item), 1);
        itemService.createItem(ItemMapper.toItemDto(item1), 1);

        assertThat(itemService.findUserItems(1), hasSize(2));
    }
}