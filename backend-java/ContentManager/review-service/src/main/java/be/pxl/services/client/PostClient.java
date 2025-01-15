package be.pxl.services.client;

import be.pxl.services.api.data.PostRequest;
import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.config.FeignConfig;
import be.pxl.services.domain.Post;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "post-service", configuration = FeignConfig.class)
public interface PostClient {

    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id);
    @GetMapping("/api/posts/pending")
    List<Post> getPendingPosts();

    @PostMapping("/api/posts/{postId}/approve")
    void approvePost(@PathVariable("postId") Long postId, ReviewRequest reviewRequest);

    @PutMapping("/api/posts/{id}/status")
    void updatePostStatus(@PathVariable("id") Long id, @RequestBody ReviewRequest reviewRequest);

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest postRequest);
}
