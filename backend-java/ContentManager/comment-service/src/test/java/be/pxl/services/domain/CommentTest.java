package be.pxl.services.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    void createComment_success() {
        Comment comment = Comment.builder()
                .content("Test Comment")
                .author("Test Author")
                .build();

        assertEquals("Test Comment", comment.getContent());
        assertEquals("Test Author", comment.getAuthor());
    }
}
