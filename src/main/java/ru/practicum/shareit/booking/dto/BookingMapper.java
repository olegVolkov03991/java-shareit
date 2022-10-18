package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Integer bookerId, Status status) {
        Booking booking = new Booking();
        booking.setItemId(bookingDto.getItemId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBookerId(bookerId);
        booking.setStatus(status);
        return booking;
    }

    public static BookingFullDto toBookingFullDto(Booking booking, User booker, Item item) {
        return new BookingFullDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booker,
                item
        );
    }
}
