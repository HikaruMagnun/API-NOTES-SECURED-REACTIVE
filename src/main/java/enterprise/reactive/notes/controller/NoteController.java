package enterprise.reactive.notes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import enterprise.reactive.notes.models.dto.NoteCoreDto;
import enterprise.reactive.notes.models.dto.NoteFullDto;
import enterprise.reactive.notes.models.dto.NoteUpdateDto;
import enterprise.reactive.notes.models.entirty.Note;
import enterprise.reactive.notes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteController {

        private final NoteService noteService;

        @PostMapping("/create")
        public Mono<ResponseEntity<Note>> createNote(@Valid @RequestBody NoteCoreDto noteCoreDto,
                        @AuthenticationPrincipal Authentication authentication) {
                return noteService.createNote(noteCoreDto, Long.valueOf(authentication.getDetails().toString()))
                                .map(createdNote -> ResponseEntity.status(HttpStatus.CREATED).body(createdNote));
        }

        @GetMapping("/active")
        public Flux<NoteFullDto> getActiveNotes(@AuthenticationPrincipal Authentication authentication) {
                return noteService.getActiveNotes(Long.valueOf(authentication.getDetails().toString()), false);
        }

        @GetMapping("/archived")
        public Flux<NoteFullDto> getArchivedNotes(@AuthenticationPrincipal Authentication authentication) {
                return noteService.getActiveNotes(Long.valueOf(authentication.getDetails().toString()), true);
        }

        @PutMapping("/{id}")
        public Mono<ResponseEntity<Note>> updateNote(@PathVariable(required = true) Long id,
                        @RequestBody NoteUpdateDto updatedNote,
                        @AuthenticationPrincipal Authentication authentication) {
                return noteService.updateNote(id, updatedNote, Long.valueOf(authentication.getDetails().toString()))
                                .map(updated -> ResponseEntity.ok(updated))
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public Mono<ResponseEntity<String>> deleteNote(@PathVariable(required = true) Long id,
                        @AuthenticationPrincipal Authentication authentication) {
                return noteService.deleteNote(id, Long.valueOf(authentication.getDetails().toString()))
                                .then(Mono.just(ResponseEntity.ok()
                                                .body("Note " + id + " eliminated successfully")))
                                .onErrorResume(e -> Mono.just(ResponseEntity
                                                .status(HttpStatus.NOT_FOUND)
                                                .body("Note " + id + " not found or error during deletion: "
                                                                + e.getMessage())));
        }

        @PatchMapping("/{id}/archive-toggle")
        public Mono<ResponseEntity<Note>> toggleArchive(@PathVariable(required = true) Long id,
                        @AuthenticationPrincipal Authentication authentication) {
                return noteService.toggleArchive(id, Long.valueOf(authentication.getDetails().toString()))
                                .map(updated -> ResponseEntity.ok(updated))
                                .defaultIfEmpty(ResponseEntity.notFound().build());
        }
}