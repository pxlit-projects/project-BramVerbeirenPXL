package be.pxl.services.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void postBuilder_createsPostSuccessfully() {
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .author("Author Name")
                .build();

        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Content", post.getContent());
        assertEquals("Author Name", post.getAuthor());
    }

    @Test
    void testEqualsAndHashCode() {
        Post post1 = new Post(1L, "Title", "Content", "Author", null, null, false, true, null);
        Post post2 = new Post(1L, "Title", "Content", "Author", null, null, false, true, null);

        assertEquals(post1, post2);
        assertEquals(post1.hashCode(), post2.hashCode());
    }

    @Test
    void testToString() {
        Post post = Post.builder().title("Test Title").content("Test Content").build();
        assertTrue(post.toString().contains("Test Title"));
    }
}
