package com.example.connectOFarm.service;

import com.example.connectOFarm.dto.OrderDto;
import com.example.connectOFarm.models.Order;
import com.example.connectOFarm.models.OrderStatus;
import com.example.connectOFarm.models.Post;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.repository.OrderRepository;
import com.example.connectOFarm.repository.PostRepository;
import com.example.connectOFarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public OrderDto placeOrder(Long postId, Long consumerId, Double quantity) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User consumer = userRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        if (post.getQuantityAvailable() < quantity) {
            throw new RuntimeException("Insufficient quantity available");
        }

        Double totalPrice = quantity * post.getPricePerUnit();

        Order order = Order.builder()
                .quantity(quantity)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .post(post)
                .user(consumer)
                .build();

        // Optionally reduce quantity here or on confirmation
        // post.setQuantityAvailable(post.getQuantityAvailable() - quantity);
        // postRepository.save(post);

        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    public OrderDto confirmOrder(Long orderId, Long farmerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getPost().getUser().getId().equals(farmerId)) {
            throw new RuntimeException("Not authorized to confirm this order");
        }

        order.setStatus(OrderStatus.CONFIRMED);

        // Deduct quantity on confirmation if not done on placement
        Post post = order.getPost();
        if (post.getQuantityAvailable() < order.getQuantity()) {
            throw new RuntimeException("Insufficient quantity to confirm order");
        }
        post.setQuantityAvailable(post.getQuantityAvailable() - order.getQuantity());
        postRepository.save(post);

        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    public List<OrderDto> getOrdersForConsumer(Long consumerId) {
        return orderRepository.findByUserId(consumerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersForFarmer(Long farmerId) {
        return orderRepository.findByFarmerId(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .postId(order.getPost().getId())
                .postTitle(order.getPost().getTitle())
                .userId(order.getUser().getId())
                .consumerName(order.getUser().getName())
                .consumerPhone(order.getUser().getPhone())
                .build();
    }
}
