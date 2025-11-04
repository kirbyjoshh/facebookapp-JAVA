package com.bondoc.facebookapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        post.setId(null); // ensure client can't set id
        Post saved = postRepository.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(p -> ResponseEntity.ok(p))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post incoming) {
        return postRepository.findById(id).map(existing -> {
            existing.setAuthor(incoming.getAuthor());
            existing.setContent(incoming.getContent());
            existing.setImageUrl(incoming.getImageUrl());
            Post saved = postRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DeleteS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return postRepository.findById(id).map(p -> {
            postRepository.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}