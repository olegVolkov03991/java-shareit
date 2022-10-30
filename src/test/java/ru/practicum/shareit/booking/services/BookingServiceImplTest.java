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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
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

    UserDto userDto = UserDto.builder()
            .id(1)
            .name("qwe")
            .email("qwe@mail.ru")
            .build();

    ItemDto itemDto = ItemDto.builder()
            .id(1)
            .owner(1)
            .available(true)
            .description("qwe")
            .name("qwe")
            .build();

    @Test
    void createBooking() {
        booking.setItemId(1);
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStatus(Status.WAITING);
        booking.setBookerId(1);

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserMapper.toUser(userDto)));

        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemMapper.toItem(itemDto, 2)));

        User user = UserMapper.toUser(userDto);
        Item item = ItemMapper.toItem(itemDto, 2);

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        bookingFullDto.setId(null);

        Assertions.assertEquals(bookingService.createBooking(bookingDto, 1), bookingFullDto);
    }

    @Test
    void getById() {
        booking.setItemId(1);
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStatus(Status.WAITING);
        booking.setBookerId(1);

        User user = UserMapper.toUser(userDto);
        Item item = ItemMapper.toItem(itemDto, 2);

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        Mockito.when(bookingRepository.getById(anyInt())).thenReturn(booking);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemMapper.toItem(itemDto, 2)));


        Assertions.assertEquals(bookingFullDto, bookingService.getById(1, 1));
    }
}