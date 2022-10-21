package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@SpringBootTest(
        properties = "db.name=testDB",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;

    @Mock
    UserRepository userRepository;

    User saveUser = makeUser("qwe@mail.ru", "qwe");

    @Test
    void createUser() {
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        saveUser.setId(1);
        UserDto userDto = UserMapper.toUserDto(saveUser);

        Mockito
                .when(userRepository.save(Mockito.any()))
                .thenReturn(saveUser);
        // when
        User checkUser = UserMapper.toUser(userService.createUser(userDto));
        // then
        Assertions.assertEquals(saveUser, checkUser);
    }

    @Test
    void getUser() {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(saveUser));

        UserDto userDto = userService.getUser(1);
        User searchUser = UserMapper.toUser(userDto);
        Assertions.assertEquals(saveUser, searchUser, "хуйня твой тест");
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        User saveUser1 = makeUser("1qwe@mail.ru", "1qwe");
        saveUser1.setId(1);
        User saveUser2 = makeUser("2qwe@mail.ru", "2qwe");
        saveUser2.setId(2);
        User saveUser3 = makeUser("3qwe@mail.ru", "3qwe");
        saveUser3.setId(3);

        users.addAll(Arrays.asList(saveUser1, saveUser2, saveUser3));

        List<UserDto> users1 = userService.getAllUsers();
        List<UserDto> searchUsers = userService.getAllUsers();
        Assertions.assertEquals(searchUsers, users);
    }

    @Test
    void delete() {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.of(saveUser));
        Assertions.assertEquals(true, userService.delete(1));
    }

    public User makeUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}