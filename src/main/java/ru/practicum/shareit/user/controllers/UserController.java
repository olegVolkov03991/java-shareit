package ru.practicum.shareit.user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto createUsers(@Valid @RequestBody UserDto userDto) {
        return userServiceImpl.createUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable Integer id) throws Exception {
        return userServiceImpl.updateUser(user, id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userServiceImpl.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userServiceImpl.delete(id);
    }
}
