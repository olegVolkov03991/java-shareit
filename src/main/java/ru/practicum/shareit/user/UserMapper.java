package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User toUpdateUser(User user, User updateUser) {
        user.setName(updateUser.getName() == null ? user.getName() : updateUser.getName());
        user.setEmail(updateUser.getEmail() == null ? user.getEmail() : updateUser.getEmail());
        return user;
    }

    public static List<UserDto> toUsersDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
