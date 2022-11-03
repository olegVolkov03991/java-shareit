package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserDto createUser(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        repository.save(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(UserDto user, Integer id) {
        if (repository.getById(id) == null) {
            throw new NotFoundException("user not found");
        }
        User newUser = repository.getById(id);
        if (user.getEmail() != null) {
            if (validationEmailForUser(user.getEmail()))
                validationEmail(user.getEmail());
            newUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        repository.save(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto getUser(Integer id) {
        User user = repository.findById(id).orElseThrow(ObjectNotFoundException::new);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return UserMapper.toUsersDto(users);
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    private boolean validationEmailForUser(String email) {
        for (User user : repository.findAll()) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    private boolean validationEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("please check email");
        }
        return true;
    }
}
