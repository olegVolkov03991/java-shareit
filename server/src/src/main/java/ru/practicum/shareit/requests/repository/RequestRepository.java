package ru.practicum.shareit.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.model.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Collection<Request> findByRequestor(Integer id);
}
