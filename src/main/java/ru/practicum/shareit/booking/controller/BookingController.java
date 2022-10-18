package ru.practicum.shareit.booking.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.services.BookingServiceImpl;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping
    public BookingFullDto createBooking(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingServiceImpl.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Optional<BookingFullDto> update(@PathVariable Integer bookingId, @RequestParam @NonNull Boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingServiceImpl.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto findById(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingServiceImpl.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingFullDto> findUserBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceImpl.findUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingFullDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceImpl.findOwnerBookings(userId, state);
    }
}
