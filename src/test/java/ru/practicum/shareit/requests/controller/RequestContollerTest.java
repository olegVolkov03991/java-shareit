package ru.practicum.shareit.requests.controller;

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
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentsMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.services.ItemServicesImpl;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.requests.services.RequestServiceImpl;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestContoller.class)
class RequestContollerTest {

    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private RequestServiceImpl requestService;
    @MockBean
    private ItemServicesImpl itemServices;
    @MockBean
    private UserController userController;
    @MockBean
    private CommentsMapper commentsMapper;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private RequestRepository requestRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    ItemDto itemDto = ItemDto.builder()
            .owner(1)
            .available(true)
            .description("qwe")
            .name("qwe")
            .id(1)
            .build();

    RequestDto requestDto = new RequestDto(1, "qwe", 1, LocalDateTime.now());

    @Test
    void create() throws Exception {
        Mockito.when(requestService.create(any(), any())).thenReturn(requestDto);

        mockMvc.perform(getContentWithPostMethod("/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(requestDto.getRequestor())));
    }

    @Test
    void getById() throws Exception {
        Item item = ItemMapper.toItem(itemDto, 1);
        Collection<Item> items = new ArrayList<>();
        items.add(item);
        RequestFullDto requestFullDto = RequestMapper.toRequestFullDto(requestDto, items);
        Mockito.when(requestService.getById(anyInt(), anyInt())).thenReturn(requestFullDto);

        mockMvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(requestFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void getRequestsAll() throws Exception {
        Item item = ItemMapper.toItem(itemDto, 1);
        Collection<Item> items = new ArrayList<>();
        Collection<RequestFullDto> requestFullDtos = new ArrayList<>();
        items.add(item);
        RequestFullDto requestFullDto = RequestMapper.toRequestFullDto(requestDto, items);
        requestFullDtos.add(requestFullDto);
        Mockito.when(requestService.getRequestsAll(anyInt())).thenReturn(requestFullDtos);

        mockMvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(requestFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void getAllRequests() throws Exception {
        Item item = ItemMapper.toItem(itemDto, 1);
        Collection<Item> items = new ArrayList<>();
        List<RequestFullDto> requestFullDtos = new ArrayList<>();
        items.add(item);
        RequestFullDto requestFullDto = RequestMapper.toRequestFullDto(requestDto, items);
        requestFullDtos.add(requestFullDto);

        Mockito.when(requestService.getAll(anyInt(), anyInt(), anyInt())).thenReturn(requestFullDtos);

        mockMvc.perform(get("/requests/all")
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    MockHttpServletRequestBuilder getContentWithPostMethod(String url) throws JsonProcessingException {
        return post(url)
                .content(objectMapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HEADER_USER_ID, 1);
    }
}