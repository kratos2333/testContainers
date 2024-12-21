package com.kevin.testcontainers.controllers;

import com.kevin.testcontainers.entities.Note;
import com.kevin.testcontainers.exceptions.NoteNotFoundException;
import com.kevin.testcontainers.repos.NoteRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
class NoteController {

    private static final Logger log = LoggerFactory.getLogger(NoteController.class);
    private final NoteRepository repository;

    public NoteController(NoteRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    List<Note> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Note> findById(@PathVariable Integer id) {
        return Optional.ofNullable(repository.findById(id).orElseThrow(NoteNotFoundException::new));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    Note save(@RequestBody @Valid Note post) {
        return repository.save(post);
    }

    @PutMapping("/{id}")
    Note update(@PathVariable Integer id, @RequestBody Note post) {
        Optional<Note> existing = repository.findById(id);
        if(existing.isPresent()) {
            Note updatedPost = new Note(existing.get().id(),
                    existing.get().userId(),
                    post.title(),
                    post.body(),
                    existing.get().version());

            return repository.save(updatedPost);
        } else {
            throw new NoteNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
