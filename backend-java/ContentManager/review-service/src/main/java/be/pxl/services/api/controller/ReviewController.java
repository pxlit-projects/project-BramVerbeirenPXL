package be.pxl.services.api.controller;

import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;
import be.pxl.services.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    // Haal alle posts op die wachten op beoordeling
    @GetMapping("/pending")
    public ResponseEntity<List<Post>> getPendingReviews() {
        List<Post> reviews = reviewService.getPendingReviews();
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // Endpoint om een post goed te keuren
    @PostMapping("/{postId}/approve")
    public ResponseEntity<Review> approvePost(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.approvePost(postId, reviewRequest));
    }

    // Endpoint om een post af te wijzen, met een opmerking
    @PostMapping("/{postId}/reject")
    public ResponseEntity<Review> rejectPost(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.rejectPost(postId, reviewRequest));
    }
}