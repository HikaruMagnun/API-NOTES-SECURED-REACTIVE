package enterprise.reactive.security.models.enums;

/*import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;*/

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    /*
     * @WritingConverter
     * public static class RoleWriteConverter implements Converter<Role, String> {
     * 
     * @Override
     * public String convert(Role role) {
     * return role.name(); // Convierte el enum a su nombre (cadena)
     * }
     * }
     * 
     * @ReadingConverter
     * public static class RoleReadConverter implements Converter<String, Role> {
     * 
     * @Override
     * public Role convert(String source) {
     * return Role.valueOf(source); // Convierte la cadena al enum
     * }
     * }
     */
}
