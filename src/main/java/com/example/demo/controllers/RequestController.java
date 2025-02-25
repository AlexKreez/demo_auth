package com.example.demo.controllers;

import com.example.demo.domain.entityUser.RequestEntity;
import com.example.demo.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<RequestEntity> createRequest(@RequestParam Long itemId) {
        return ResponseEntity.ok(requestService.createRequest(itemId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<RequestEntity> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.approveRequest(id));
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    @PutMapping("/reject/{id}")
    public ResponseEntity<RequestEntity> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }

    @PreAuthorize("hasAuthority('INVENTORY_MANAGER')")
    @GetMapping("/get_all")
    public ResponseEntity<List<RequestEntity>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @PreAuthorize("hasAnyAuthority('INVENTORY_MANAGER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestEntity>> getUserRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getUserRequests(userId));
    }
}
