package ru.practicum.shareit.user.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    private final User user = new User();
    private final User user1 = new User();
    private final User user2 = new User();
    private final UserDto userDto = UserDto.builder()
            .id(1)
            .name("qwe")
            .email("qwe@mail.ru")
            .build();


    @Test
    void createUser() {
        user.setId(1);
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userService.createUser(userDto), userDto);

    }

    @Test
    void getUser() {
        user.setId(1);
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Assertions.assertEquals(userService.getUser(1), UserMapper.toUserDto(user));
    }

    @Test
    void getAllUsers() {
        user.setId(1);
        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user1.setId(2);
        user1.setName("qwe2");
        user1.setEmail("qwe2@mail.ru");
        user2.setId(3);
        user2.setName("qwe3");
        user2.setEmail("qwe3@mail.ru");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assertions.assertEquals(userService.getAllUsers(), UserMapper.toUsersDto(users));
    }
}