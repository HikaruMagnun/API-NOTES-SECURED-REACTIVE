package enterprise.reactive.notes.repository;

import java.util.List;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import enterprise.reactive.notes.models.entirty.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Flux<Category> findByUserId(Long userId);

    Mono<Category> findByIdAndUserId(Long id, Long userId);

    // Método para encontrar categorías por una lista de IDs
    Flux<Category> findByIdIn(List<Long> ids);
}
