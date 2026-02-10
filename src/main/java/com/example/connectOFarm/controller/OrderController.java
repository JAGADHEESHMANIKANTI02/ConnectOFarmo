package com.example.connectOFarm.controller;

import com.example.connectOFarm.dto.OrderDto;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/consumer/orders")
    @PreAuthorize("hasRole('CONSUMER')")
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody OrderDto request,
            @AuthenticationPrincipal User user) {
        // request.getPostId() and quantity should be in body
        return ResponseEntity.ok(orderService.placeOrder(request.getPostId(), user.getId(), request.getQuantity()));
    }

    @PutMapping("/farmer/orders/confirm/{id}/")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<OrderDto> confirmOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.confirmOrder(id, user.getId()));
    }

    @GetMapping("/consumer/orders")
    @PreAuthorize("hasRole('CONSUMER')")
    public ResponseEntity<List<OrderDto>> getConsumerOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersForConsumer(user.getId()));
    }

    @GetMapping("/farmer/orders")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<List<OrderDto>> getFarmerOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersForFarmer(user.getId()));
    }
}
