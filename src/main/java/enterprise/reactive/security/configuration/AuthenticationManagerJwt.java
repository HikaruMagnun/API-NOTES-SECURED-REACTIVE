package enterprise.reactive.security.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import enterprise.reactive.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationManagerJwt implements ReactiveAuthenticationManager {

        private final JwtService jwtService;

        @SuppressWarnings("unchecked")
        @Override
        public Mono<Authentication> authenticate(Authentication authentication) {
                return Mono.just(authentication)
                                .map(auth -> jwtService.getClaims(auth.getCredentials().toString()))
                                .log()
                                .onErrorResume(e -> Mono.error(
                                                new Exception(HttpStatus.UNAUTHORIZED.toString() + " bad token ")))
                                .map(claims -> {
                                        // Extraer el sujeto (username) del token
                                        String username = claims.getSubject();

                                        // Extraer el userId del token
                                        String userId = Optional.ofNullable(claims.get("userId", String.class))
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "UserId not found in token"));

                                        // Extraer los roles del token
                                        List<Map<String, String>> roleObjects = Optional
                                                        .ofNullable(claims.get("role", List.class))
                                                        .orElse(Collections.emptyList());

                                        // Mapear los roles a SimpleGrantedAuthority
                                        List<GrantedAuthority> authorities = roleObjects.stream()
                                                        .map(role -> role.get("authority")) // Extraer el valor de
                                                                                            // "authority"
                                                        .map(SimpleGrantedAuthority::new) // Convertir a
                                                                                          // SimpleGrantedAuthority
                                                        .collect(Collectors.toList());

                                        // Crear el objeto Authentication
                                        UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
                                                        username, null, authorities);
                                        authResult.setDetails(userId); // Almacenar userId en los detalles del token

                                        return authResult;
                                });
        }
}
