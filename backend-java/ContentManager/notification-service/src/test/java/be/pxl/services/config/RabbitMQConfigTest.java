package be.pxl.services.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigTest {

    private final RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

    @Test
    void postQueue_returnsQueue() {
        Queue postQueue = rabbitMQConfig.postQueue();

        assertThat(postQueue.getName()).isEqualTo("post.queue");
        assertThat(postQueue.isDurable()).isTrue();
    }

    @Test
    void commentQueue_returnsQueue() {
        Queue commentQueue = rabbitMQConfig.commentQueue();

        assertThat(commentQueue.getName()).isEqualTo("comment.queue");
        assertThat(commentQueue.isDurable()).isTrue();
    }
}