package ru.practicum.shareit.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadReqestException;
import ru.practicum.shareit.exceptions.ObjectAlreadyException;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDbStorage;

import java.util.List;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public UserDto createUser(UserDto user) {
        if (validationEmailForUser(user.getEmail()) && validationEmail(user.getEmail())) {
            return UserMapper.toUserDto(userDbStorage.createUser(UserMapper.toUser(user)));
        }
        throw new ObjectAlreadyException("this email already exists");
    }

    public List<UserDto> getAllUsers() {
        return UserMapper.toUsersDto(userDbStorage.getAllUsers());
    }

    public UserDto updateUser(Long id, UserDto user) {
        if (user.getEmail() != null) {
            if (validationEmailForUser(user.getEmail()) && validationEmail(user.getEmail())) {
                return UserMapper.toUserDto(userDbStorage.updateUser(UserMapper.toUpdateUser(
                        userDbStorage.getUser(id), UserMapper.toUser(user))));
            }
            throw new ObjectAlreadyException("this email already exists");
        }
        return UserMapper.toUserDto(userDbStorage.updateUser(UserMapper.toUpdateUser(
                userDbStorage.getUser(id), UserMapper.toUser(user))));
    }

    public UserDto getUser(Long id) {
        return UserMapper.toUserDto(userDbStorage.getUser(id));
    }

    public void deleteuser(Long id) {
        userDbStorage.deleteUser(id);
    }

    private boolean validationEmailForUser(String email) {
        for (UserDto userDto : getAllUsers()) {
            if (userDto.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    private boolean validationEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new BadReqestException();
        }
        return true;
    }
}
