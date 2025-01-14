package be.pxl.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(classes = ReviewServiceApplication.class)
public class ReviewServiceApplicationTest {
    @Test
    void main_doesNotThrow() {
        assertThatCode(() -> ReviewServiceApplication.main(new String[]{})).doesNotThrowAnyException();
    }
}
