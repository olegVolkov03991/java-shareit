package ru.practicum.shareit.GeneratorId;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class GeneratorId {
    public Long idGenerator(Long id) {
        return ++id;
    }
}
