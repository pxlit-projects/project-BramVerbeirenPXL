package be.pxl.services.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String POST_QUEUE = "post.queue";
    public static final String COMMENT_QUEUE = "comment.queue";

    @Bean
    public Queue postQueue() {
        return new Queue(POST_QUEUE, true);
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE, true);
    }
}
