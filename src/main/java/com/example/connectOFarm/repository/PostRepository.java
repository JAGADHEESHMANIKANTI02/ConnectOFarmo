package com.example.connectOFarm.repository;

import com.example.connectOFarm.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findAllByOrderByCreatedAtDesc();
}
