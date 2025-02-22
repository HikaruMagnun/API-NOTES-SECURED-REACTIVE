package enterprise.reactive.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import enterprise.reactive.security.models.dto.LoginUserDto;
import enterprise.reactive.security.models.dto.RegisterUserDto;
import enterprise.reactive.security.models.dto.TokenDto;
import enterprise.reactive.security.models.entity.User;
import enterprise.reactive.security.models.entity.UserRepository;
import enterprise.reactive.security.models.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;

        private final JwtService jwtService;

        private final PasswordEncoder passwordEncoder;

        public Mono<TokenDto> login(LoginUserDto dto) {
                return userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getUsername())
                                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                                .map(user -> {
                                        log.info("Inicio de sesión exitoso para el usuario: {}", user.getUsername());
                                        return new TokenDto(jwtService.generateToken(user));
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                        log.error("Credenciales inválidas para el usuario: {}", dto.getUsername());
                                        return Mono.error(new Exception(
                                                        HttpStatus.BAD_REQUEST.toString() + " bad credentials"));
                                }));
        }

        public Mono<User> register(RegisterUserDto dto, Role role) {
                List<String> roles = new ArrayList<>();
                roles.add(Role.ROLE_USER.name());
                if (role == Role.ROLE_ADMIN) {
                        roles.add(Role.ROLE_ADMIN.name());
                }
                String roleString = String.join(", ", roles);
                User user = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .dni(dto.getDni())
                                .role(roleString)
                                .build();
                Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                                .hasElement();
                return userExists
                                .flatMap(exists -> {
                                        if (exists) {
                                                log.error("Registro fallido: El nombre de usuario o correo electrónico ya está en uso: {} / {}",
                                                                user.getUsername(), user.getEmail());
                                                return Mono.error(new Exception(HttpStatus.BAD_REQUEST.toString()
                                                                + " username or email already in use"));
                                        } else {
                                                log.info("Registro exitoso para el usuario: {}", user.getUsername());
                                                return userRepository.save(user);
                                        }
                                });
        }

        public Mono<User> findByUsername(String username) {
                return userRepository.findByUsername(username)
                                .switchIfEmpty(Mono.empty());
        }
}