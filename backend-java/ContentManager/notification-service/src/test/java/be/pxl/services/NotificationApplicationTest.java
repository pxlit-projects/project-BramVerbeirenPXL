package be.pxl.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class NotificationApplicationTest {

    @Test
    void main_doesNotThrow() {
        assertThatCode(() -> NotificationApplication.main(new String[]{})).doesNotThrowAnyException();
    }
}