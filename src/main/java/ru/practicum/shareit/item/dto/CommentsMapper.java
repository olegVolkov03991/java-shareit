package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comments;

import java.time.LocalDateTime;

public class CommentsMapper {
    public static CommentsDto toCommentDto(Comments comments, String authorName) {
        return new CommentsDto(
                comments.getId(),
                comments.getText(),
                authorName,
                comments.getCreated()
        );
    }

    public static Comments toComment(CommentsDto commentDto, Integer itemId, Integer authorId) {
        Comments comment = new Comments();
        comment.setText(commentDto.getText());
        comment.setItemId(itemId);
        comment.setAuthorId(authorId);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
