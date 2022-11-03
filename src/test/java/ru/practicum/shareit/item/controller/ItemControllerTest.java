package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.services.ItemServicesImpl;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private  ItemMapper itemMapper;
    @MockBean
    private ItemServicesImpl itemServices;
    @MockBean
    private  UserController userController;
    @MockBean
    private  CommentsMapper commentsMapper;
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

    private  ItemDto itemDto = ItemDto.builder()
            .owner(1)
            .available(true)
            .description("qwe")
            .name("qwe")
            .id(1)
            .build();

    @Test
    void createItem() throws Exception {
        Mockito.when(itemServices.createItem(any(), anyInt())).thenReturn(itemDto);

        mockMvc.perform(getContentWithPostMethod("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())));
    }

    @Test
    void updateItem() throws Exception {
        Mockito.when(itemServices.updateItem(any(), anyInt(), anyInt())).thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner", is(itemDto.getOwner())));

    }

    @Test
    void findItemById() throws Exception {
        ItemFullDto itemFullDto = new ItemFullDto();
        Mockito.when(itemServices.getItem(anyInt(), anyInt())).thenReturn(Optional.of(itemFullDto));

        mockMvc.perform(get("/items/1")
                        .content(objectMapper.writeValueAsString(itemFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemFullDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFullDto.getAvailable())));

    }

    @Test
    void getAllItems() throws Exception {
        ItemFullDto itemFullDto = new ItemFullDto();
        Collection<ItemFullDto> itemFullDtos = new ArrayList<>();
        itemFullDtos.add(itemFullDto);
        Mockito.when(itemServices.findUserItems(anyInt())).thenReturn(itemFullDtos);

        mockMvc.perform(get("/items")
                        .content(objectMapper.writeValueAsString(itemFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void searchItems() throws Exception {
        Item item = ItemMapper.toItem(itemDto, 1);
        Collection<Item> items = new ArrayList<>();
        items.add(item);
        Mockito.when(itemServices.searchItems(anyString())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header(HEADER_USER_ID, 1)
                        .queryParam("text", "searchText"))
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