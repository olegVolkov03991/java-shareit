package ru.practicum.shareit.booking.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingIntegrationTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final EntityManager entityManager;

    Booking booking = BookingMapper.toBooking(new BookingDto(LocalDateTime.of(
                    2023, 1, 1, 1, 1, 1),
                    LocalDateTime.of(
                            2024, 1, 1, 1, 1, 1),
                    1),
            1,
            Status.WAITING);

    private User user = UserMapper.toUser(UserDto.builder()
            .email("qwe@mail.ru")
            .name("qwe")
            .id(1)
            .build());

    private User user2 = UserMapper.toUser(UserDto.builder()
            .email("qwe2@mail.ru")
            .name("qwe2")
            .id(2)
            .build());

    private Item item = ItemMapper.toItem(ItemDto.builder()
            .name("qwe")
            .id(1)
            .description("qwe")
            .available(true)
            .owner(1)
            .build(), 1);

    @Test
    void createBooking() {

        userService.createUser(UserMapper.toUserDto(user));
        userService.createUser(UserMapper.toUserDto(user2));
        itemService.createItem(ItemMapper.toItemDto(item), 2);

        BookingFullDto bookingFullDto = bookingService.createBooking(BookingMapper.toBookingDto(booking), 1);

        TypedQuery<Booking> query = entityManager.createQuery("select booking from Booking booking where booking.id = : id", Booking.class);

        Booking booking = query.setParameter("id", bookingFullDto.getId()).getSingleResult();

        assertThat(bookingFullDto.getStatus(), equalTo(booking.getStatus()));
    }
}