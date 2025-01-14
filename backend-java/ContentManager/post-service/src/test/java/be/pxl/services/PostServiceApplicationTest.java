package be.pxl.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class PostServiceApplicationTest {
    @Test
    void main_doesNotThrow() {
        assertThatCode(() -> PostServiceApplication.main(new String[]{})).doesNotThrowAnyException();
    }
}
