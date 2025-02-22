package enterprise.reactive.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public Mono<String> publicEndpoint(Authentication expression) {
        log.info(expression.getDetails().toString());
        return Mono.just("this is a public endpoint");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/private")
    public Mono<String> privateEndpoint() {
        return Mono.just("this is a private endpoint");
    }

}
