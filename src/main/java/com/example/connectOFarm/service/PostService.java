package com.example.connectOFarm.service;

import com.example.connectOFarm.dto.PostDto;
import com.example.connectOFarm.models.Post;
import com.example.connectOFarm.models.User;
import com.example.connectOFarm.repository.PostRepository;
import com.example.connectOFarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto createPost(PostDto dto, Long farmerId, MultipartFile imageFile) throws IOException {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        Post post = Post.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .cropName(dto.getCropName())
                .quantityAvailable(dto.getQuantityAvailable())
                .pricePerUnit(dto.getPricePerUnit())
                .image(imageFile != null ? imageFile.getBytes() : null)
                .createdAt(LocalDateTime.now())
                .user(farmer)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToDto(post);
    }

    public List<PostDto> getPostsByFarmer(Long farmerId) {
        return postRepository.findByUserId(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deletePost(Long id, Long farmerId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(farmerId)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .cropName(post.getCropName())
                .quantityAvailable(post.getQuantityAvailable())
                .pricePerUnit(post.getPricePerUnit())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .farmerName(post.getUser().getName())
                .farmerPhone(post.getUser().getPhone())
                .farmerEmail(post.getUser().getEmail())
                .build();
    }
}
