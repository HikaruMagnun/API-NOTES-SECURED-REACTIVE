package enterprise.reactive.notes.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import enterprise.reactive.notes.models.dto.NoteCoreDto;
import enterprise.reactive.notes.models.dto.NoteFullDto;
import enterprise.reactive.notes.models.dto.NoteUpdateDto;
import enterprise.reactive.notes.models.entirty.Note;
import enterprise.reactive.notes.models.entirty.NoteCategory;
import enterprise.reactive.notes.repository.CategoryRepository;
import enterprise.reactive.notes.repository.NoteCategoryRepository;
import enterprise.reactive.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteCategoryRepository noteCategoryRepository;
    private final CategoryRepository categoryRepository;

    public Mono<Note> createNote(NoteCoreDto noteDto, Long userId) {
        log.info("Creating new note with title: {} for user: {}", noteDto.getTitle(), userId);
        return noteRepository.save(Note.builder()
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .archived(false)
                .build());
    }

    public Flux<NoteFullDto> getActiveNotes(Long userId, boolean archived) {
        return noteRepository.findByUserIdAndArchived(userId, archived)
                .flatMap(note ->

                noteCategoryRepository.findByNoteId(note.getId())
                        .map(NoteCategory::getCategoryId) // Extraer los categoryId
                        .collectList() // Convertir el Flux<NoteCategory> en una List<Long>
                        .flatMap(categoryIds ->
                        // Paso 2: Buscar las categorías correspondientes usando los categoryId
                        categoryRepository.findByIdIn(categoryIds).collectList()
                                .map(categories -> NoteFullDto.builder()
                                        .id(note.getId())
                                        .title(note.getTitle())
                                        .content(note.getContent())
                                        .archived(note.isArchived())
                                        .userId(note.getUserId())
                                        .categories(categories) // Asignar la lista de categorías
                                        .build())));
    }

    public Mono<Note> updateNote(Long id, NoteUpdateDto updatedNote, Long userId) {
        return noteRepository.findByIdAndUserId(id, userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Note not found")))
                .map(existingNote -> existingNote.toBuilder()
                        .title(updatedNote.getTitle())
                        .content(updatedNote.getContent())
                        .archived(updatedNote.isArchived())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .flatMap(noteRepository::save);
    }

    public Mono<Void> deleteNote(Long id, Long userId) {
        return noteRepository.findByIdAndUserId(id, userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Note not found")))
                .flatMap(noteRepository::delete);
    }

    public Mono<Note> toggleArchive(Long id, Long userId) {
        return noteRepository.findByIdAndUserId(id, userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Note not found")))
                .map(note -> note.toBuilder()
                        .archived(!note.isArchived())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .flatMap(noteRepository::save);
    }

}
