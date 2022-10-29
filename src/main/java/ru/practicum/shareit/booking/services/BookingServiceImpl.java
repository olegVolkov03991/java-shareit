package ru.practicum.shareit.booking.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.states.States;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingFullDto createBooking(BookingDto bookingDto, Integer bookerId) {
        validateBooking(bookingDto);
        User booker = validateUser(bookerId);
        Item item = validateItem(bookingDto, bookerId);
        Booking booking = BookingMapper.toBooking(bookingDto, bookerId, Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingFullDto(booking, booker, item);
    }

    @Override
    public Optional<BookingFullDto> updateBooking(Integer id, Integer ownerId, Boolean approved) {
        validateUser(ownerId);
        Booking booking = validateBooking(id, ownerId, null);
        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("status not equal WAITING");
        }
        User booker = validateUser(booking.getBookerId());
        Item item = validateItem(BookingMapper.toBookingDto(booking), booking.getBookerId());
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return Optional.of(BookingMapper.toBookingFullDto(bookingRepository.save(booking), booker, item));
    }

    @Override
    public BookingFullDto getById(Integer bookingId, Integer userId) {
        try {
            Booking booking = bookingRepository.getById(bookingId);
            User booker = validateUser(booking.getBookerId());
            Item item = validateItem(BookingMapper.toBookingDto(booking), booking.getBookerId());
            BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, booker, item);
            return bookingFullDto;
        } catch (Exception e) {
            throw new NotFoundException("тут вообще беда");
        }
    }

    @Override
    public Collection<BookingFullDto> findUserBookings(Integer bookerId, String state, Integer from, Integer size) {
        validationPage(from, size);
        User booker = validateUser(bookerId);
        if (state.equals(States.ALL.name())) {
            return bookingRepository.findAllByBookerId(bookerId,getPageRequest(from, size), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else if (state.equals(States.CURRENT.name())) {
            return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                            bookerId, getPageRequest(from, size), LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else if (state.equals(States.PAST.name())) {
            return bookingRepository.findAllByBookerIdAndEndBefore(
                            bookerId, getPageRequest(from, size), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else if (state.equals(States.FUTURE.name())) {
            return bookingRepository.findAllByBookerIdAndStartAfter(
                            bookerId, getPageRequest(from, size), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else if (state.equals(States.WAITING.name())) {
            return bookingRepository.findAllByBookerIdAndStatus(
                            bookerId, getPageRequest(from, size), Status.WAITING, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else if (state.equals(States.REJECTED.name())) {
            return bookingRepository.findAllByBookerIdAndStatus(
                            bookerId, getPageRequest(from, size), Status.REJECTED, Sort.by(Sort.Direction.DESC, "start"))
                    .stream()
                    .map(booking -> BookingMapper.toBookingFullDto(booking, booker, itemRepository.findById(booking.getItemId()).get()))
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }
    }

    @Override
    public Collection<BookingFullDto> findOwnerBookings(Integer ownerId, String state, Integer from, Integer size) {
        validationPage(from, size);
        validateUser(ownerId);
        try {
            States bookingState = States.valueOf(state);
        } catch (Exception e) {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }
        return bookingRepository.findAllByOwnerId(ownerId, getPageRequest(from, size), state, LocalDateTime.now())
                .stream()
                .map(booking -> BookingMapper.toBookingFullDto(booking,
                        userRepository.findById(booking.getBookerId()).get(),
                        itemRepository.findById(booking.getItemId()).get()))
                .collect(Collectors.toList());
    }

    private User validateUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("user not found");
        }
        return user.get();
    }

    private Item validateItem(BookingDto bookingDto, Integer bookerId) {
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) {
            throw new NotFoundException("item is empty");
        } else if (!item.get().getAvailable()) {
            throw new ValidationException("itemAvailable is false");
        } else if (bookingRepository.findByItemIdAndStatusAndStartBeforeAndEndAfter(
                bookingDto.getItemId(), Status.APPROVED, bookingDto.getEnd(), bookingDto.getStart()).size() > 0) {
            throw new NotFoundException("error");
        } else if (bookerId != null && item.get().getOwner().equals(bookerId)) {
            throw new NotFoundException("please check bookerId");
        }
        return item.get();
    }

    private Booking validateBooking(Integer bookingId, Integer ownerId, Integer bookerId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new NotFoundException("booking not found exception");
        } else {
            Item item = itemRepository.findById(booking.get().getItemId()).get();
            if (bookerId == null && !item.getOwner().equals(ownerId)) {
                throw new NotFoundException("please check bookerId");
            } else if (ownerId == null && !booking.get().getBookerId().equals(bookerId)) {
                throw new NotFoundException("please check owner");
            } else if (bookerId != null && ownerId != null
                    && !booking.get().getBookerId().equals(bookerId)
                    && !item.getOwner().equals(ownerId)) {
                throw new NotFoundException("please check owner and bookerId");
            }
        }
        return booking.get();
    }

    private void validateBooking(BookingDto bookingDto) {
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("start before now");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("end before now");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("end before start");
        }
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    }

    private void validationPage(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("from or size < 0");
        }
    }
}
