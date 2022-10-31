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
import ru.practicum.shareit.user.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    private final UserDto userDto = UserDto.builder()
            .id(1)
            .name("qwe")
            .email("qwe@mail.ru")
            .build();

    private final User user = UserMapper.toUser(userDto);
    private final User user1 = UserMapper.toUser(userDto);


    @Test
    void createUser() {
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userService.createUser(userDto), userDto);

    }

    @Test
    void getUser() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Assertions.assertEquals(userService.getUser(1), UserMapper.toUserDto(user));
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        user1.setId(2);
        user1.setName("qwe2");
        user1.setEmail("qwe2@mail.ru");
        users.add(user);
        users.add(user1);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assertions.assertEquals(userService.getAllUsers(), UserMapper.toUsersDto(users));
    }
}