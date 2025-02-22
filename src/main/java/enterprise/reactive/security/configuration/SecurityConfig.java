package enterprise.reactive.security.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import enterprise.reactive.security.jwt.JwtFilter;
import enterprise.reactive.security.models.dto.RegisterUserDto;
import enterprise.reactive.security.models.enums.Role;
import enterprise.reactive.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
        return http
                .authorizeExchange(authorizeExchangeCustomizer -> authorizeExchangeCustomizer
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/auth/register-admin").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(securityContextRepository)
                .formLogin(formLoginCustomizer -> formLoginCustomizer.disable())
                .logout(logoutCustomizer -> logoutCustomizer.disable())
                .httpBasic(httpBasicCustomizer -> httpBasicCustomizer.disable())
                .csrf(csrfCustomizer -> csrfCustomizer.disable())
                .build();
    }

    @Bean
    public CommandLineRunner initDefaultUser(UserService userService) {
        return args -> {
            userService.findByUsername("HikaruMagnun")
                    .switchIfEmpty(userService.register(
                            RegisterUserDto.builder()
                                    .username("HikaruMagnun")
                                    .email("carlosdiazramos25@hotmai.com")
                                    .password("password")
                                    .dni("72016836").build(),
                            Role.ROLE_ADMIN))
                    .doOnSuccess(user -> log.info("Default admin user exists or was created: {}", user.getUsername()))
                    .doOnError(error -> log.error("Failed to check/register default admin user", error))
                    .subscribe();
        };
    }
}
