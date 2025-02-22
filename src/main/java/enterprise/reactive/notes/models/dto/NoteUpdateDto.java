package enterprise.reactive.notes.models.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NoteUpdateDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String content;
    private List<Long> categoryIds;
    @NotNull
    private boolean archived;
}
