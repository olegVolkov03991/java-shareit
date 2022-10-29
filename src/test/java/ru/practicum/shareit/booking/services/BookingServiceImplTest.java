package ru.practicum.shareit.booking.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    private final Booking booking = new Booking();
    private final User user = new User();
    private final Item item = new Item();
    private final User user2 = new User();

    @Test
    void createBooking() {
        booking.setItemId(1);
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStatus(Status.WAITING);
        booking.setBookerId(1);

        user.setId(1);
        user.setEmail("qwe@mail.ru");
        user.setName("qwe");

        user2.setName("qwe2");
        user2.setEmail("qwe2@mail.ru");
        user2.setId(2);

        item.setId(1);
        item.setOwner(2);
        item.setAvailable(true);
        item.setDescription("qwe");
        item.setName("qwe");

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        bookingFullDto.setId(null);

        Assertions.assertEquals(bookingService.createBooking(bookingDto, 1), bookingFullDto);
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getById() {
        booking.setItemId(1);
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStatus(Status.WAITING);
        booking.setBookerId(1);

        user.setId(1);
        user.setEmail("qwe@mail.ru");
        user.setName("qwe");

        user2.setName("qwe2");
        user2.setEmail("qwe2@mail.ru");
        user2.setId(2);

        item.setId(1);
        item.setOwner(2);
        item.setAvailable(true);
        item.setDescription("qwe");
        item.setName("qwe");

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        Assertions.assertEquals(bookingFullDto, bookingService.getById(1, 1));
    }
}