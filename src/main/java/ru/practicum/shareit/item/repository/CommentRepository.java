package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comments;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comments, Integer> {
    Collection<Comments> findAllByItemIdOrderByCreatedDesc(Integer itemId);
}
