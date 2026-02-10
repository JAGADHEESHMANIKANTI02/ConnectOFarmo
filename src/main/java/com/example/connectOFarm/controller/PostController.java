package com.example.connectOFarm.controller;

import com.example.connectOFarm.dto.PostDto;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/farmer/posts", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<PostDto> createPost(
            @RequestPart("post") PostDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal User user) throws java.io.IOException {


        return ResponseEntity.ok(postService.createPost(dto, user.getId(), image));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/farmer/posts/{id}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        postService.deletePost(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/farmer/posts/my-posts")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<List<PostDto>> getMyPosts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.getPostsByFarmer(user.getId()));
    }
}
