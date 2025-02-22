package enterprise.reactive.notes.models.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NoteCoreDto {

    @NotBlank(message = "Title is mandatory")
    private String title;
    private String content;
    private List<Long> categoryIds;

}
