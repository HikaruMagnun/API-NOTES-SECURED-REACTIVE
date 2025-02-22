package enterprise.reactive.notes.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCoreDto {
    @NotBlank(message = "Name is mandatory")
    private String name;

}
