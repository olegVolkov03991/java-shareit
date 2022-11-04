package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentsDto {
    private Integer id;
    @NotBlank(message = "Text is required")
    private String text;
    private String authorName;
    private LocalDateTime created;
}
