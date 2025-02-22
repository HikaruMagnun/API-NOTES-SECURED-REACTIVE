package enterprise.reactive.notes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import enterprise.reactive.notes.service.NoteCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteCategoryController {

    private final NoteCategoryService noteCategoryService;

    @PostMapping("/{noteId}/categories/{categoryId}")
    public Mono<ResponseEntity<String>> addCategoryToNote(
            @PathVariable Long noteId,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal Authentication authentication) {

        return noteCategoryService
                .addCategoryToNote(noteId, categoryId, Long.valueOf(authentication.getDetails().toString()))
                .map(result -> ResponseEntity.ok("Category added to note"))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{noteId}/categories/{categoryId}")
    public Mono<ResponseEntity<String>> removeCategoryFromNote(
            @PathVariable Long noteId,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal Authentication authentication) {
        return noteCategoryService
                .removeCategoryFromNote(noteId, categoryId, Long.valueOf(authentication.getDetails().toString()))
                .then(Mono.just(ResponseEntity.ok()
                        .body("Note " + categoryId + " eliminated successfully")))
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Note " + categoryId + " not found or error during deletion: "
                                + e.getMessage())));
    }
}