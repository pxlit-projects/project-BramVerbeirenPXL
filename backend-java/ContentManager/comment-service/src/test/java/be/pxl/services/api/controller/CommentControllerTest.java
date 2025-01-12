package be.pxl.services.api.controller;

import be.pxl.services.api.data.CommentRequest;
import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
import be.pxl.services.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void getCommentsByPost_returnsListOfComments() throws Exception {
        List<Comment> comments = List.of(new Comment(1L, 1L, "Author", "Content", null));
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andDo(print());
    }

    @Test
    void addComment_createsNewComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest("Author", "Content");
        Comment comment = new Comment(1L, 1L, "Author", "Content", null);
        when(commentService.addComment(1L, commentRequest)).thenReturn(comment);

        mockMvc.perform(post("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"Author\",\"content\":\"Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.content").value("Content"))
                .andDo(print());
    }

    @Test
    void updateComment_updatesExistingComment() throws Exception {
        Comment updatedComment = new Comment(1L, 1L, "Author", "Updated Content", null);
        when(commentService.updateComment(1L, "Updated Content")).thenReturn(updatedComment);

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Updated Content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andDo(print());
    }

    @Test
    void deleteComment_deletesComment() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteComment_throwsExceptionIfNotFound() throws Exception {
        doThrow(new RuntimeException()).when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNotFound())  // Verwacht 404 Not Found// Controleer dat de body de juiste boodschap bevat
                .andDo(print());
    }
}
