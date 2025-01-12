package be.pxl.services.api.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @Email(message = "Author email is invalid")
    @NotBlank(message = "Email is mandatory")
    private String authorEmail;

    @NotNull(message = "Approval status is mandatory")
    private Boolean approved;

    private String rejectionComment;

    @NotNull(message = "Draft status is mandatory")
    private Boolean draft;

    public @NotNull(message = "Draft status is mandatory") Boolean isDraft() {
        return draft;
    }

    public @NotNull(message = "Approval status is mandatory") Boolean isApproved() {
        return approved;
    }
}
