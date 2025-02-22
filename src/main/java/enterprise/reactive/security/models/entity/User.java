package enterprise.reactive.security.models.entity;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("users")
public class User implements UserDetails {

    @Id
    private Long ID;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private String dni;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role.split(", ")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
