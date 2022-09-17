package ru.practicum.shareit.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDbStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public User updateUser(Long id, User user) {
        return userDbStorage.updateUser(id, user);
    }

    public User getUser(Long id) {
        return userDbStorage.getUser(id);
    }

    public User deleteuser(Long id) {
        return userDbStorage.deleteUser(id);
    }
}
