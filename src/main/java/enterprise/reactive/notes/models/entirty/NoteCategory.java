package enterprise.reactive.notes.models.entirty;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("note_categories")
public class NoteCategory {
    @Id
    private Long id;
    @Column("note_id")
    private Long noteId;

    @Column("category_id")
    private Long categoryId;
}
