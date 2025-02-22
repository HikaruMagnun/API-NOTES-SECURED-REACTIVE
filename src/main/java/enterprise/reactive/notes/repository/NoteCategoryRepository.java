package enterprise.reactive.notes.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import enterprise.reactive.notes.models.entirty.NoteCategory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NoteCategoryRepository extends ReactiveCrudRepository<NoteCategory, Long> {
    Flux<NoteCategory> findByNoteId(Long noteId);

    Flux<NoteCategory> findByCategoryId(Long categoryId);

    Mono<Void> deleteByNoteIdAndCategoryId(Long noteId, Long categoryId);
}