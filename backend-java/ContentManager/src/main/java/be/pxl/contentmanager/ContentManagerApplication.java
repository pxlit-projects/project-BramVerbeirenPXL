package be.pxl.contentmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testcontainers.containers.RabbitMQContainer;

@SpringBootApplication
public class ContentManagerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ContentManagerApplication.class, args);
    }
}
