package be.pxl.services.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String author;
    private String authorEmail;
}
