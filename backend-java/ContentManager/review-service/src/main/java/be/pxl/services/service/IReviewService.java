package be.pxl.services.service;

import be.pxl.services.api.data.ReviewRequest;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.Review;

import java.util.List;

public interface IReviewService {
    List<Post> getPendingReviews();

    Review approvePost(Long postId, ReviewRequest reviewRequest);

    Review rejectPost(Long postId, ReviewRequest reviewRequest);
}
