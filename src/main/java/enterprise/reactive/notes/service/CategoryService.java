package enterprise.reactive.notes.service;

import org.springframework.stereotype.Service;

import enterprise.reactive.notes.models.dto.CategoryCoreDto;
import enterprise.reactive.notes.models.entirty.Category;
import enterprise.reactive.notes.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Mono<Category> createCategory(CategoryCoreDto categoryDto, Long userId) {
        return Mono.just(Category.builder()
                .name(categoryDto.getName())
                .userId(userId)
                .build())
                .flatMap(categoryRepository::save);
    }

    public Flux<Category> getCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Mono<Void> deleteCategory(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .flatMap(categoryRepository::delete);
    }
}