package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectAlreadyException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component

public class UserDbStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User createUser(User user) {
        User newUser = new User();
        validationUser(user);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setId(generatedId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }


    @Override
    public User updateUser(Long id, User user) {
        User newUser = new User();
        if (users.containsKey(id)) {
            fullUpdate(id, user, newUser);
            updateUserEmail(id, user, newUser);
            updateUserName(id, user, newUser);
        }
        return newUser;
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
    public Collection<User> getAllUsers() {
        return users.values();
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

    public void updateUserName(Long id, User user, User newUser) {
        if (user.getEmail() == null && user.getName() != null) {
            newUser.setId(id);
            newUser.setName(user.getName());
            newUser.setEmail(users.get(id).getEmail());
            users.put(id, newUser);
        }
    }

    public void updateUserEmail(Long id, User user, User newUser) {
        if (user.getName() == null && user.getEmail() != null) {
            validationUser(user);
            newUser.setId(id);
            newUser.setName(users.get(id).getName());
            newUser.setEmail(user.getEmail());
            users.put(id, newUser);
        }
    }

    public void fullUpdate(Long id, User user, User newUser) {
        if (user.getEmail() != null && user.getName() != null) {
            validationUser(user);
            newUser.setId(id);
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            users.put(id, newUser);
        }
    }
}
