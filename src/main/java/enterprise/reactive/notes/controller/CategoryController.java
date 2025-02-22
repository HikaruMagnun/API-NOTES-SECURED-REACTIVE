package enterprise.reactive.notes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import enterprise.reactive.notes.models.dto.CategoryCoreDto;
import enterprise.reactive.notes.models.entirty.Category;
import enterprise.reactive.notes.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public Mono<ResponseEntity<Category>> createCategory(@RequestBody CategoryCoreDto category,
            @AuthenticationPrincipal Authentication authentication) {
        return categoryService.createCategory(category, Long.valueOf(authentication.getDetails().toString()))
                .map(createdCategory -> ResponseEntity.status(HttpStatus.CREATED).body(createdCategory));
    }

    @GetMapping("/all")
    public Flux<Category> getCategories(@AuthenticationPrincipal Authentication authentication) {
        return categoryService.getCategories(Long.valueOf(authentication.getDetails().toString()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteCategory(@PathVariable Long id,
            @AuthenticationPrincipal Authentication authentication) {
        return categoryService.deleteCategory(id, Long.valueOf(authentication.getDetails().toString()))
                .then(Mono.just(ResponseEntity.ok(String.format("Category deleted {id}", id))))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}