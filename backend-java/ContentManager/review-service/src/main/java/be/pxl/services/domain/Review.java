package be.pxl.services.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private Long postId;
    @Setter
    @Getter
    private boolean approved;
    @Setter
    @Getter
    private String rejectionComment;
}