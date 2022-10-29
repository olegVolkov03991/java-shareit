package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.Request;
import ru.practicum.shareit.requests.services.RequestService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestContollerTest {

    private final RequestService requestService;
    private final EntityManager entityManager;
    private final UserService userService;

    Item item = new Item();
    User user = new User();
    Item item2 = new Item();
    User user2 = new User();

    @Test
    void create() {
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        userService.createUser(UserMapper.toUserDto(user));
        RequestDto requestDto = requestService.create(ItemMapper.toItemDto(item), 1);
        TypedQuery<Request> query = entityManager.createQuery("select req from Request req where req.id = : id", Request.class);
        Request request = query.setParameter("id", requestDto.getId()).getSingleResult();

        assertThat(requestDto.getRequestor(), equalTo(request.getRequestor()));
        assertThat(requestDto.getDescription(), equalTo(request.getDescription()));


    }

    @Test
    void getById() {
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        Collection<Item> items = new ArrayList<>();
        items.add(item);


        userService.createUser(UserMapper.toUserDto(user));
        RequestDto requestDto = requestService.create(ItemMapper.toItemDto(item), 1);

        TypedQuery<Request> query = entityManager.createQuery("select req from Request req where req.id = : id", Request.class);
        Request request = query.setParameter("id", requestDto.getId()).getSingleResult();
        RequestFullDto requestFullDto = RequestMapper.toRequestFullDto(RequestMapper.toRequestDto(request), items);

        RequestFullDto requestFullDto1 = requestService.getById(1, 1);
        requestFullDto1.setItems(items);

        assertThat(requestFullDto, equalTo(requestFullDto1));
    }

    @Test
    void getRequestsAll() {
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setAvailable(true);
        item.setDescription("qweqwe");
        item.setName("qwe");
        item.setId(1);
        item.setOwner(1);

        item2.setAvailable(true);
        item2.setDescription("qwe22qwe");
        item2.setName("qwe");
        item2.setId(1);
        item2.setOwner(1);

        userService.createUser(UserMapper.toUserDto(user));
        requestService.create(ItemMapper.toItemDto(item), 1);
        requestService.create(ItemMapper.toItemDto(item2), 1);

        assertThat(requestService.getRequestsAll(1), hasSize(2));
    }
}