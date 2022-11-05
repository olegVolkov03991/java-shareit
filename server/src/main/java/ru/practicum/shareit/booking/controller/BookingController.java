package ru.practicum.shareit.booking.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.services.BookingServiceImpl;

import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping
    public BookingFullDto createBooking(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingServiceImpl.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Optional<BookingFullDto> update(@PathVariable int bookingId, @RequestParam @NonNull Boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingServiceImpl.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto findById(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingServiceImpl.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingFullDto> findUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                       @PositiveOrZero int from,
                                                       @RequestParam(required = false, defaultValue = "20")
                                                       @PositiveOrZero int size) {
        return bookingServiceImpl.findUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingFullDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(defaultValue = "ALL") String state, @RequestParam(required = false, defaultValue = "0")
                                                        @PositiveOrZero int from,
                                                        @RequestParam(required = false, defaultValue = "20")
                                                        @PositiveOrZero int size) {
        return bookingServiceImpl.findOwnerBookings(userId, state, from, size);
    }
}
