package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final EntityManager entityManager;
    private final UserService userService;

    private UserDto userDto = UserDto.builder()
            .name("qwe")
            .email("qwe@mail.ru")
            .build();

    @Test
    void createUsers() {

        UserDto userDto1 = userService.createUser(userDto);
        TypedQuery<User> query = entityManager.createQuery(
                "select user from User user where user.id = : id", User.class
        );
        User user = query.setParameter("id", userDto1.getId()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }


}