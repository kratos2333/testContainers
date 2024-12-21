package com.kevin.testcontainers.repos;

import com.kevin.testcontainers.entities.Note;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends ListCrudRepository<Note, Integer> {
    Note findByTitle(String title);
}

