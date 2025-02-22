package enterprise.reactive.notes.models.entirty;

import java.time.LocalDateTime;

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
@Table("notes")
public class Note {
    @Id
    private Long id;
    private String title;
    private String content;
    private boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column("user_id")
    private Long userId;

    public Note.NoteBuilder toBuilder() {
        return Note.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .archived(this.archived)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .userId(this.userId);
    }
}