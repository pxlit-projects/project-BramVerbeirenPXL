package be.pxl.services.repository;

import be.pxl.services.domain.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
    }

    @Test
    void findByPostId_returnsComments() {
        Comment comment = new Comment(1L, 1L, "Author", "Content", null);
        commentRepository.save(comment);

        List<Comment> comments = commentRepository.findByPostId(1L);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getAuthor()).isEqualTo("Author");
    }

    @Test
    void save_savesComment() {
        Comment comment = new Comment(null, 1L, "Author", "New Content", null);
        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("New Content");
    }
}
