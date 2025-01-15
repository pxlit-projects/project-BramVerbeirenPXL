package be.pxl.services.api.controller;

import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.service.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/published")
    public ResponseEntity<List<Post>> getPublishedPosts() {
        List<Post> posts = postService.getPublishedPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/drafts")
    public ResponseEntity<Post> saveDraft(@RequestBody @Valid PostRequest postRequest) {
        Post draft = postService.saveDraft(postRequest);
        return ResponseEntity.status(201).body(draft);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        return ResponseEntity.status(201).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest postRequest) {
        Post updatedPost = postService.updatePost(id, postRequest);
        if (updatedPost == null) {
            return ResponseEntity.status(404).build(); // Return NOT_FOUND als de post niet bestaat
        }
        return ResponseEntity.ok(updatedPost);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Post>> getPendingPosts() {
        List<Post> posts = postService.getPendingPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Post>> filterPosts(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) LocalDate date) {
        List<Post> filteredPosts = postService.filterPosts(content, author, date);
        if (filteredPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredPosts);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Post> updatePostStatus(@PathVariable Long id, @RequestBody @Valid ReviewRequest reviewRequest) {
        Post updatedPost = postService.updatePostStatus(id, reviewRequest);
        if (updatedPost == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(updatedPost);
    }
}
