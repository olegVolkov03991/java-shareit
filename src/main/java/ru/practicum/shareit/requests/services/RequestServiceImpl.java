package ru.practicum.shareit.requests.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.RequestDto;
import ru.practicum.shareit.requests.dto.RequestFullDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.Request;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestDto create(ItemDto itemDto, Integer userId) {
        validationDescription(itemDto.getDescription());
        validationRequestor(userId);

        Request request = new Request();
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setDescription(itemDto.getDescription());
        request.setRequestor(userId);

        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestFullDto getById(Integer id, Integer userId) {
        validationRequestId(id);
        validationRequestor(userId);
        Collection<Item> items = itemRepository.findItemByRequestId(id);
        RequestDto requestDto = RequestMapper.toRequestDto(requestRepository.getById(id));
        return RequestMapper.toRequestFullDto(requestDto, items);
    }

    @Override
    public Collection<RequestFullDto> getRequestsAll(Integer userId) {
        validationRequestor(userId);
        Collection<Item> items = itemRepository.findByRequestId(userId);
        return requestRepository.findByRequestor(userId).stream()
                .map(request -> RequestMapper.toRequestFullDto(RequestMapper.toRequestDto(request), items))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestFullDto> getAll(Integer userId, Integer from, Integer size) {
        validationPage(from, size);
        Collection<Item> items = itemRepository.findByOwnerAndRequestIdNotNull(userId);
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не найден."));
        return requestRepository.findAll(getPageRequest(from, size)).stream()
                .filter(itemRequest -> !Objects.equals(itemRequest.getRequestor(), requester.getId()))
                .map(request -> RequestMapper.toRequestFullDto(RequestMapper.toRequestDto(request), items))
                .collect(Collectors.toList());
    }

    private void validationPage(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("from or size < 0");
        }
    }

    private void validationRequestId(Integer id) {
        if (requestRepository.findById(id).isEmpty()) {
            throw new NotFoundException("request not found");
        }
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    }


    private void validationDescription(String text) {
        if (text == null) {
            throw new ValidationException("descroption =null");
        }
    }

    private void validationRequestor(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("userId not found");
        }
    }
}
