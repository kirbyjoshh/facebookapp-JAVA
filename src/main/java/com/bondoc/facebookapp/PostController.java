package com.bondoc.facebookapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://localhost:5173", "https://facebookapp-react.onrender.com"})
public class PostController {

    private final PostRepository repo;

    public PostController(PostRepository repo) {
        this.repo = repo;
    }

    // Create a post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post saved = repo.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all posts
    @GetMapping
    public List<Post> getAllPosts() {
        return repo.findAll();
    }

    // Get one post
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    // Update a post (replace)
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post updated) {
        return repo.findById(id).map(p -> {
            p.setAuthor(updated.getAuthor());
            p.setContent(updated.getContent());
            p.setImageUrl(updated.getImageUrl());
            return repo.save(p);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    // Partial update (PATCH)
    @PatchMapping("/{id}")
    public Post patchPost(@PathVariable Long id, @RequestBody Post partial) {
        return repo.findById(id).map(p -> {
            if (partial.getAuthor() != null) p.setAuthor(partial.getAuthor());
            if (partial.getContent() != null) p.setContent(partial.getContent());
            if (partial.getImageUrl() != null) p.setImageUrl(partial.getImageUrl());
            return repo.save(p);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return repo.findById(id).map(p -> {
            repo.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}