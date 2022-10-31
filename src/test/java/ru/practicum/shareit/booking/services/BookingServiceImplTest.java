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

    private Booking booking = BookingMapper.toBooking(new BookingDto(LocalDateTime.of(
                    2023, 1, 1, 1, 1, 1)
                    , LocalDateTime.of(
                    2024, 1, 1, 1, 1, 1)
                    , 1)
            , 1
            , Status.WAITING);

    private User user = UserMapper.toUser(UserDto.builder()
            .email("qwe@mail.ru")
            .name("qwe")
            .id(1)
            .build());

    private Item item = ItemMapper.toItem(ItemDto.builder()
            .name("qwe")
            .id(1)
            .description("qwe")
            .available(true)
            .owner(1)
            .build(), 1);

    private ItemDto itemDto = ItemMapper.toItemDto(item);

    @Test
    void createBooking() {

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemMapper.toItem(itemDto, 2)));

        item.setOwner(2);

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        bookingFullDto.setId(null);

        Assertions.assertEquals(bookingService.createBooking(bookingDto, 1), bookingFullDto);
    }

    @Test
    void getById() {

        item.setOwner(2);

        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);

        Mockito.when(bookingRepository.getById(anyInt())).thenReturn(booking);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemMapper.toItem(itemDto, 2)));

        Assertions.assertEquals(bookingFullDto, bookingService.getById(1, 1));
    }
}