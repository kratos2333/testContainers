package com.kevin.testcontainers.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.testcontainers.entities.Notes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.kevin.testcontainers.repos.NoteRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final ObjectMapper objectMapper;
    private final NoteRepository repository;

    public DataLoader(ObjectMapper objectMapper, NoteRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(repository.count() == 0){
            String NOTES_JSON = "/data/notes.json";
            log.info("Loading notes into database from JSON: {}", NOTES_JSON);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(NOTES_JSON)) {
                Notes content = objectMapper.readValue(inputStream, Notes.class);
                repository.saveAll(content.notes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
