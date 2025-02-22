package enterprise.reactive.notes.models.dto;

import java.util.List;

import enterprise.reactive.notes.models.entirty.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteFullDto {
    private Long id;
    private String title;
    private String content;
    private boolean archived;
    private Long userId;
    private List<Category> categories;
}
