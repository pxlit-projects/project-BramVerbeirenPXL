package be.pxl.services.repository;

import be.pxl.services.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();  // Schoon de database voor elke test
    }

    @Test
    void findByDraftFalseAndApprovedTrue_returnsPublishedPosts() {
        Post post = new Post(null, "Title", "Content", "Author", null, LocalDate.now(), false, true, null);
        postRepository.save(post);

        List<Post> publishedPosts = postRepository.findByDraftFalseAndApprovedTrue();

        assertEquals(1, publishedPosts.size());
        assertTrue(publishedPosts.get(0).isApproved());
    }

    @Test
    void findByDraftFalseAndApprovedFalse_returnsPendingPosts() {
        Post post = new Post(null, "Title", "Content", "Author", null, LocalDate.now(), false, false, null);
        postRepository.save(post);

        List<Post> pendingPosts = postRepository.findByDraftFalseAndApprovedFalse();

        assertEquals(1, pendingPosts.size());
        assertFalse(pendingPosts.get(0).isApproved());
    }
}
