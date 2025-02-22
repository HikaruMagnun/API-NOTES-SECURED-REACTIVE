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
@Table("categories")
public class Category {
    @Id
    private Long id;
    private String name;
    @Column("user_id")
    private Long userId;
}