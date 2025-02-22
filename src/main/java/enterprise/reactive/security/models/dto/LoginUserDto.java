package enterprise.reactive.security.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class LoginUserDto {
    private String username;
    private String password;
}
