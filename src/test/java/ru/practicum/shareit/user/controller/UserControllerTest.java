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
            .id(1)
            .name("qwe")
            .email("qwe@mail.ru")
            .build();
    private UserDto userDto2 = UserDto.builder()
            .id(2)
            .name("qwe2")
            .email("qw2e@mail.ru")
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

    @Test
    void getAllUsers() {
        UserDto userDtoOne = userService.createUser(userDto);
        UserDto userDtoTwo = userService.createUser(userDto2);


        userService.createUser(userDtoOne);
        userService.createUser(userDtoTwo);


        assertThat(userService.getAllUsers(), hasSize(2));
        assertThat(userService.getAllUsers().get(0).getName(), equalTo(userDto.getName()));
        assertThat(userService.getAllUsers().get(0).getEmail(), equalTo(userDto.getEmail()));

        assertThat(userService.getAllUsers(), hasSize(2));
        assertThat(userService.getAllUsers().get(1).getName(), equalTo(userDto2.getName()));
        assertThat(userService.getAllUsers().get(1).getEmail(), equalTo(userDto2.getEmail()));
    }

    @Test
    void getUser() {
        userService.createUser(userDto);
        UserDto userDto2 = userService.getUser(userDto.getId());

        assertThat(userDto2.getId(), notNullValue());
        assertThat(userDto2.getName(), equalTo(userDto.getName()));
        assertThat(userDto2.getEmail(), equalTo(userDto.getEmail()));

    }
}