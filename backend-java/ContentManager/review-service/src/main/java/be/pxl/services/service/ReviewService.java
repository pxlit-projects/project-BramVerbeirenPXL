package be.pxl.services.service;


import be.pxl.services.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
//    @Autowired
//    private PostServiceClient postServiceClient;
//
//
//    public List<Post> getPendingPosts() {
//        return postServiceClient.getPendingPosts();
//    }
//
//    public Post approvePost(Long id) {
//        Post post = postServiceClient.getPostById(id);
//        post.setApproved(true);
//        postServiceClient.updatePost(id, post);
//        return post;
//    }
//
//    public Post rejectPost(Long id, String rejectionComment) {
//        Post post = postServiceClient.getPostById(id);
//        post.setApproved(false);
//        post.setRejectionComment(rejectionComment);
//        postServiceClient.updatePost(id, post);
//        return post;
//    }
}
