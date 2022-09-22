package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String name;
}
