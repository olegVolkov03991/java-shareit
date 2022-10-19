package ru.practicum.shareit.booking.services;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Collection;
import java.util.Optional;

public interface BookingService {
    BookingFullDto createBooking(BookingDto bookingDto, Integer userId);

    Optional<BookingFullDto> updateBooking(Integer bookingId, Integer userId, Boolean approved);

    BookingFullDto getById(Integer bookingId, Integer userId);

    Collection<BookingFullDto> findUserBookings(Integer bookerId, String state);

    Collection<BookingFullDto> findOwnerBookings(Integer ownerId, String state) throws ValidationException;
}
