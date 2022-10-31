package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    RequestRepository requestRepository;
    @MockBean
    BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    BookingDto bookingDto = new BookingDto(LocalDateTime.of(
            2023, 1, 1, 1, 1, 1)
            , LocalDateTime.of(
            2024, 1, 1, 1, 1, 1)
            , 1);

    User user = UserMapper.toUser(UserDto.builder()
            .id(1)
            .name("qwe")
            .email("qwe@mail.ru")
            .build());

    Item item = ItemMapper.toItem(ItemDto.builder()
            .owner(1)
            .available(true)
            .description("qwe")
            .id(1)
            .name("qwe")
            .build(), 1);

    @Test
    void createBooking() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingDto, 1, Status.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Mockito.when(bookingService.createBooking(any(), anyInt())).thenReturn(bookingFullDto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingFullDto.getId())))
                .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void update() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingDto, 1, Status.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Mockito.when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.of(bookingFullDto));

        mockMvc.perform(getContentWithPatchMethod()
                        .content(objectMapper.writeValueAsString(bookingFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingFullDto.getId())))
                .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void findById() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingDto, 1, Status.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Mockito.when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.of(bookingFullDto));

        Mockito.when(bookingService.getById(anyInt(), anyInt())).thenReturn(bookingFullDto);
        mockMvc.perform(getContentWithPatchMethod()
                        .content(objectMapper.writeValueAsString(bookingFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingFullDto.getId())))
                .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void findUserBookings() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingDto, 1, Status.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Mockito.when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.of(bookingFullDto));
        Collection<BookingFullDto> bookingFullDtos = new ArrayList<>();
        bookingFullDtos.add(bookingFullDto);

        Mockito.when(bookingService.findUserBookings(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingFullDtos);

        mockMvc.perform(getContentWithGetMethod("/bookings")
                        .content(objectMapper.writeValueAsString(bookingFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findOwnerBookings() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingDto, 1, Status.WAITING);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Mockito.when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.of(bookingFullDto));

        Collection<BookingFullDto> bookingFullDtos = new ArrayList<>();
        bookingFullDtos.add(bookingFullDto);

        Mockito.when(bookingService.findOwnerBookings(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingFullDtos);

        mockMvc.perform(getContentWithGetMethod("/bookings/owner")
                        .content(objectMapper.writeValueAsString(bookingFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    MockHttpServletRequestBuilder getContentWithPatchMethod() throws JsonProcessingException {
        return patch("/bookings/1")
                .content(objectMapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_ID, 1)
                .queryParam("approved", "true");
    }

    MockHttpServletRequestBuilder getContentWithGetMethod(String url) throws JsonProcessingException {
        return get(url)
                .content(objectMapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_ID, 1);
    }
}