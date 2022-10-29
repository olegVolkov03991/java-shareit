package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemService;
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
class BookingControllerTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final EntityManager entityManager;

    private Booking booking = new Booking();
    private User user = new User();
    private User user2 = new User();
    private Item item = new Item();


    @Test
    void createBooking() {
        booking.setStatus(Status.WAITING);
        booking.setId(1);
        booking.setBookerId(1);
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setItemId(1);

        user2.setName("qwe2");
        user2.setEmail("qwe2@mail.ru");
        user2.setId(2);

        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setDescription("qwe");
        item.setAvailable(true);
        item.setOwner(1);
        item.setName("qwe");
        item.setId(1);

        userService.createUser(UserMapper.toUserDto(user));
        userService.createUser(UserMapper.toUserDto(user2));
        itemService.createItem(ItemMapper.toItemDto(item), 2);


        BookingFullDto bookingFullDto = bookingService.createBooking(BookingMapper.toBookingDto(booking), 1);


        TypedQuery<Booking> query = entityManager.createQuery("select booking from Booking booking where booking.id = : id", Booking.class);

        Booking booking = query.setParameter("id", bookingFullDto.getId()).getSingleResult();

        assertThat(bookingFullDto.getStatus(), equalTo(booking.getStatus()));


    }

    @Test
    void findById() {

        booking.setStatus(Status.WAITING);
        booking.setId(1);
        booking.setBookerId(1);
        booking.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        booking.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setItemId(1);

        user2.setName("qwe2");
        user2.setEmail("qwe2@mail.ru");
        user2.setId(2);

        user.setName("qwe");
        user.setEmail("qwe@mail.ru");
        user.setId(1);

        item.setDescription("qwe");
        item.setAvailable(true);
        item.setOwner(1);
        item.setName("qwe");
        item.setId(1);

        userService.createUser(UserMapper.toUserDto(user));
        userService.createUser(UserMapper.toUserDto(user2));
        itemService.createItem(ItemMapper.toItemDto(item), 2);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);


        bookingService.createBooking(bookingDto, 1);

        BookingFullDto bookingFullDto = bookingService.createBooking(BookingMapper.toBookingDto(booking), 1);


        assertThat(bookingFullDto.getStatus(), equalTo(booking.getStatus()));

    }
}