package ru.practicum.shareit.requests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.Request;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    private final Request request = new Request();
    private final Request request2 = new Request();
    private final User user = new User();
    private final Item item = new Item();


    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void create() {
        request.setId(null);
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setRequestor(1);
        request.setDescription("qwe");

        user.setId(1);
        user.setEmail("qwe@mail.ru");
        user.setName("qwe");

        item.setOwner(1);
        item.setAvailable(true);
        item.setName("qwe");
        item.setId(1);
        item.setDescription("qwe");

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Assertions.assertEquals(RequestMapper.toRequestDto(request), requestService.create(ItemMapper.toItemDto(item), 1));


    }

    @Test
    void getById() {

        request.setId(null);
        request.setCreated(LocalDateTime.now().withSecond(0));
        request.setRequestor(1);
        request.setDescription("qwe");

        item.setOwner(1);
        item.setAvailable(true);
        item.setName("qwe");
        item.setId(1);
        item.setDescription("qwe");

        Collection<Item> items = new ArrayList<>();

       // items.add(item);

        RequestFullDto requestFullDto = RequestMapper.toRequestFullDto(RequestMapper.toRequestDto(request), items);

        Mockito.when(requestRepository.findById(anyInt())).thenReturn(Optional.of(request));

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Mockito.when(requestRepository.getById(anyInt())).thenReturn(request);

        Assertions.assertEquals(requestFullDto, requestService.getById(1, 1));
    }

    @Test
    void getRequestsAll() {
        request2.setId(null);
        request2.setCreated(LocalDateTime.now().withSecond(0));
        request2.setRequestor(1);
        request2.setDescription("qwe");

        user.setId(1);
        user.setEmail("qwe@mail.ru");
        user.setName("qwe");

        item.setOwner(1);
        item.setAvailable(true);
        item.setName("qwe");
        item.setId(1);
        item.setDescription("qwe");

        Collection<Item> items = new ArrayList<>();

        items.add(item);

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findByRequestId(anyInt())).thenReturn(items);

        Collection<Request> requests = new ArrayList<>();

        Mockito.when(requestRepository.findByRequestor(anyInt())).thenReturn(requests);

        Assertions.assertEquals(requests, requestService.getRequestsAll(1));
    }
}