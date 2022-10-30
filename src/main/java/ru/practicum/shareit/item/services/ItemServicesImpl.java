package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServicesImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserServiceImpl userServiceImpl;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Integer userId) {
        validationItemName(itemDto.getName());
        validationDescriptionItem(itemDto.getDescription());
        validatonAvailableItem(itemDto.getAvailable());
        validationOwnerItem(userId);
        Item newItem = ItemMapper.toItem(itemDto, userId);
        itemRepository.save(newItem);
        return ItemMapper.toItemDto(newItem);

    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Integer id, Integer userId) {
        if (!itemRepository.findById(id).orElseThrow().getOwner().equals(userId)) {
            throw new NotFoundException("item not found owner(userId)");
        }
        Item item = itemRepository.getById(id);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return text == null || text.isBlank() ? new ArrayList<>() : itemRepository.search(text);
    }

    @Override
    public Optional<ItemFullDto> getItem(Integer id, Integer userId) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return Optional.of(ItemMapper.toItemFullDto(item.get(),
                    bookingRepository.findLastBooking(id, userId, LocalDateTime.now()),
                    bookingRepository.findNextBooking(id, userId, LocalDateTime.now()),
                    commentRepository.findAllByItemIdOrderByCreatedDesc(id)
                            .stream()
                            .map(comment -> CommentsMapper.toCommentDto(comment, validateUser(comment.getAuthorId()).getName()))
                            .collect(Collectors.toList())));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<ItemFullDto> findUserItems(Integer userId) {
        return itemRepository.findByOwnerOrderByIdAsc(userId)
                .stream()
                .map(item -> ItemMapper.toItemFullDto(item,
                        bookingRepository.findLastBooking(item.getId(), userId, LocalDateTime.now()),
                        bookingRepository.findNextBooking(item.getId(), userId, LocalDateTime.now()),
                        commentRepository.findAllByItemIdOrderByCreatedDesc(item.getId())
                                .stream()
                                .map(comment -> CommentsMapper.toCommentDto(comment, validateUser(comment.getAuthorId()).getName()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentsDto> addItemComment(Integer itemId, Integer userId, CommentsDto commentDto) {
        User user = validateUser(userId);
        if (validateBookingItem(itemId, userId)) {
            Comments comment = commentRepository.save(CommentsMapper.toComment(commentDto, itemId, userId));
            return Optional.of(CommentsMapper.toCommentDto(comment, user.getName()));
        } else {
            return Optional.empty();
        }
    }

    private boolean validateBookingItem(Integer itemId, Integer userId) {
        Optional<Booking> booking = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, Status.APPROVED, LocalDateTime.now());
        if (booking.isPresent()) {
            return true;
        } else {
            throw new ValidationException("booking is present");
        }
    }

    private User validateUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ValidationException("user is present");
        }
        return user.get();
    }

    public void validationItemName(String name) {
        if (name.isEmpty()) {
            throw new ValidationException("name empty");
        }
    }

    public void validationDescriptionItem(String description) {
        if (description == null) {
            throw new ValidationException("description = null");
        }
    }

    public void validatonAvailableItem(Boolean available) {
        if (available == null) {
            throw new ValidationException("available = null");
        }
    }

    public void validationOwnerItem(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("user not found");
        }
    }
}
