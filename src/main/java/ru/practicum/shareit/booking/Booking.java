package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    Date start;
    Date end;
    Item item;
    User booker;
    enum status {
        WATTING, //новое бронирование
        APPROVED, //бронирование подтверждено владельцем
        REJECTED, //бронирование отклонено вадельцем
        CANCELED; //бронирование отменено создателем
    }
}
