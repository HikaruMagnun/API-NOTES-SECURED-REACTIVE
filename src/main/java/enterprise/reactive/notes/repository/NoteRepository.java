package enterprise.reactive.notes.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import enterprise.reactive.notes.models.entirty.Note;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NoteRepository extends ReactiveCrudRepository<Note, Long> {
    Flux<Note> findByUserIdAndArchived(Long userId, boolean archived);

    Mono<Note> findByIdAndUserId(Long id, Long userId);
}
