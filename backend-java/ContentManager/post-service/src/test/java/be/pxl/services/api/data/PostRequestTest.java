package be.pxl.services.api.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostRequestTest {

    @Test
    void createPostRequest_withValidValues() {
        PostRequest postRequest = new PostRequest("Test Title", "Test Content", "Test Author", "author@example.com", true, "approved", false);

        assertEquals("Test Title", postRequest.getTitle());
        assertEquals("Test Content", postRequest.getContent());
        assertEquals("Test Author", postRequest.getAuthor());
        assertEquals("author@example.com", postRequest.getAuthorEmail());
    }
}
