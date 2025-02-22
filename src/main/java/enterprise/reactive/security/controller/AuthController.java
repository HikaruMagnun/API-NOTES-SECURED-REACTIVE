package enterprise.reactive.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import enterprise.reactive.security.models.dto.LoginUserDto;
import enterprise.reactive.security.models.dto.RegisterUserDto;
import enterprise.reactive.security.models.dto.TokenDto;
import enterprise.reactive.security.models.entity.User;
import enterprise.reactive.security.models.enums.Role;
import enterprise.reactive.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public Mono<TokenDto> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        return userService.login(loginUserDto);
    }

    @PostMapping("/register")
    public Mono<User> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return userService.register(registerUserDto, Role.ROLE_USER);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/register-admin")
    public Mono<User> registerAdmin(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return userService.register(registerUserDto, Role.ROLE_ADMIN);
    }
}
