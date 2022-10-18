package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Collection<Booking> findAllByBookerId(Integer bookerId, Sort states);

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Integer bookerId, LocalDateTime start, LocalDateTime end, Sort states);

    Collection<Booking> findAllByBookerIdAndEndBefore(Integer bookerId, LocalDateTime end, Sort states);

    Collection<Booking> findAllByBookerIdAndStartAfter(Integer bookerId, LocalDateTime start, Sort states);

    Collection<Booking> findAllByBookerIdAndStatus(Integer bookerId, Status status, Sort states);

    Collection<Booking> findByItemIdAndStatusAndStartBeforeAndEndAfter(
            Integer itemId, Status status, LocalDateTime start, LocalDateTime end);

    @Query(" select b from Item i, Booking b " +
            " where i.owner = ?1 " +
            "   and b.itemId = i.id " +
            "   and (?2 = 'ALL' or " +
            "        (?2 = 'CURRENT' and ?3 between b.start and b.end) or " +
            "        (?2 = 'PAST' and ?3 > b.end) or " +
            "        (?2 = 'FUTURE' and ?3 < b.start) or " +
            "        (?2 = 'WAITING' and b.status = ?2) or " +
            "        (?2 = 'REJECTED' and b.status = ?2) " +
            "       )" +
            " order by b.start desc")
    Collection<Booking> findAllByOwnerId(Integer ownerId, String state, LocalDateTime localDateTime);

    @Query(value = "select b.* from bookings b " +
            " where b.item_id = :itemId " +
            "   and b.booker_id <> :userId " +
            "   and b.start_date < :now " +
            " order by b.start_date asc limit 1",
            nativeQuery = true)
    Optional<Booking> findLastBooking(Integer itemId, Integer userId, LocalDateTime now);

    @Query(value = "select b.* from bookings b " +
            " where b.item_id = :itemId " +
            "   and b.booker_id <> :userId " +
            "   and b.start_date > :now " +
            " order by b.start_date desc limit 1",
            nativeQuery = true)
    Optional<Booking> findNextBooking(Integer itemId, Integer userId, LocalDateTime now);

    Optional<Booking> findByItemIdAndBookerIdAndStatusAndEndBefore(Integer itemId, Integer userId, Status status, LocalDateTime now);
}
