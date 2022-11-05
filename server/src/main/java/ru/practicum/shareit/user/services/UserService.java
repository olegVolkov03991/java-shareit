package ru.practicum.shareit.user.services;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUser(Integer id);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, Integer id) throws Exception;

    boolean delete(Integer id);
}
