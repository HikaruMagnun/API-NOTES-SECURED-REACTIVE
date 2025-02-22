package enterprise.reactive.notes.service;

import org.springframework.stereotype.Service;

import enterprise.reactive.notes.models.entirty.NoteCategory;
import enterprise.reactive.notes.repository.CategoryRepository;
import enterprise.reactive.notes.repository.NoteCategoryRepository;
import enterprise.reactive.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteCategoryService {

        private final NoteRepository noteRepository;
        private final CategoryRepository categoryRepository;
        private final NoteCategoryRepository noteCategoryRepository;

        public Mono<Void> addCategoryToNote(Long noteId, Long categoryId, Long userId) {
                return noteRepository.findByIdAndUserId(noteId, userId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Note not found")))
                                .flatMap(note -> categoryRepository.findById(categoryId)
                                                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                                                .flatMap(category -> noteCategoryRepository.save(NoteCategory.builder()
                                                                .noteId(noteId)
                                                                .categoryId(categoryId)
                                                                .build()))
                                                .then());
        }

        public Mono<Void> removeCategoryFromNote(Long noteId, Long categoryId, Long userId) {
                return noteRepository.findByIdAndUserId(noteId, userId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Note not found")))
                                .flatMap(note -> noteCategoryRepository.deleteByNoteIdAndCategoryId(noteId,
                                                categoryId));
        }
}