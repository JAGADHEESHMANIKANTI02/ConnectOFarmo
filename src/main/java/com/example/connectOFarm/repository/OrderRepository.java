package com.example.connectOFarm.repository;

import com.example.connectOFarm.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.post.user.id = :farmerId")
    List<Order> findByFarmerId(@Param("farmerId") Long farmerId);
}
