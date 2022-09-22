package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectAlreadyException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component

public class UserDbStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User createUser(User user) {
        validationUser(user);
        user.setId(generatedId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else throw new ObjectNotFoundException();
        return user;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            log.error("user does not exist");
        }
        return users.get(id);
    }

    @Override
    public User deleteUser(Long id) {
        users.remove(id);
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public Long generatedId() {
        return ++id;
    }

    public void validationUser(User user) {
        if (!users.isEmpty()) {
            for (Map.Entry<Long, User> users : users.entrySet()) {
                User user1 = users.getValue();
                if (user1.getEmail().contains(user.getEmail())) {
                    throw new ObjectAlreadyException("this user already exists");
                }

            }
        }
    }
}