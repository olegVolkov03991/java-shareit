package ru.practicum.shareit.booking.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
