package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.model.Request;
import ru.practicum.shareit.requests.services.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.services.UserService;
import ru.practicum.shareit.user.services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestContollerTest {

    private final RequestService requestService;
    private final EntityManager entityManager;
    private final UserServiceImpl userService;
    @Mock
    private final UserRepository userRepository;

    UserDto userDto = UserDto.builder()
            .name("qwe")
            .email("qwe@mail.ru")
            .build();

    ItemDto itemDto = ItemDto.builder()
            .owner(1)
            .available(true)
            .description("qwe")
            .name("qwe")
            .build();

    @Test
    void create() {

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserMapper.toUser(userDto)));
        UserDto userDto1 = userService.createUser(userDto);
        RequestDto requestDto = requestService.create(itemDto, userDto1.getId());
        TypedQuery<Request> query = entityManager.createQuery("select req from Request req where req.id = : id", Request.class);
        Request request = query.setParameter("id", requestDto.getId()).getSingleResult();

        assertThat(requestDto.getRequestor(), equalTo(request.getRequestor()));
        assertThat(requestDto.getDescription(), equalTo(request.getDescription()));


    }

}