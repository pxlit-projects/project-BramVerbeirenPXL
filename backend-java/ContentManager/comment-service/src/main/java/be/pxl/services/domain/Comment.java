package be.pxl.services.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @NotBlank(message = "Auteur mag niet leeg zijn")
    private String author;

    @NotBlank(message = "Inhoud mag niet leeg zijn")
    private String content;

    private LocalDateTime createdDate = LocalDateTime.now();
}
