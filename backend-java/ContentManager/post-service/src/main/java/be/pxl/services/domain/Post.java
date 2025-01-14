package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String title;
    @Getter
    private String content;
    @Getter
    private String author;
    @Getter
    private String authorEmail;
    @Getter
    private LocalDate createdDate;
    @Getter
    @Setter
    private boolean draft;
    @Setter
    private boolean approved;
    @Setter
    private String rejectionComment;

    public Post(String title, String content, String author, String authorEmail, LocalDate createdDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorEmail = authorEmail;
        this.createdDate = createdDate;
        this.approved = false;
    }

}
