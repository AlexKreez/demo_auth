package com.example.demo.services;

import com.example.demo.domain.entityUser.RequestEntity;
import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.inventory.Item;
import com.example.demo.domain.inventory.RequestStatus;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private static final Logger LOGGER =  LoggerFactory.getLogger(RequestService.class);
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public RequestEntity createRequest(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("❌ Инструмент не найден"));
        //String username = jwtService.extractUsername(jwt);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("❌ Пользователь не найден"));

        RequestEntity request = new RequestEntity();
        request.setItem(item);
        request.setRequestedBy(user);
        request.setStatus(RequestStatus.PENDING);
        requestRepository.save(request);

        LOGGER.info("✅ Заявка ID {} создана пользователем {}", request.getId(), username);

        return request;
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    @Transactional
    public RequestEntity approveRequest(Long requestId) {
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Заявка не найдена"));
        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        LOGGER.info("✅ Заявка ID {} одобрена пользователем {}", requestId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return request;
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    @Transactional
    public RequestEntity rejectRequest(Long requestId) {
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Заявка не найдена"));
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        LOGGER.info("❌ Заявка ID {} отклонена пользователем {}", requestId,
                SecurityContextHolder.getContext().getAuthentication().getName());

        return request;
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    public List<RequestEntity> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<RequestEntity> getUserRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Пользователь не найден"));
        return requestRepository.findByRequestedBy(user);
    }
}
