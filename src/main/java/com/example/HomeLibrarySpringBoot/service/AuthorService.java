package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    void addAuthor(Author author);

    void removeAuthor(int id);

    Author getAuthor(int id);

    void updateAuthor(Author author);

    List<Author> getAuthors();

    List<Author> getAuthorsById(Iterable<Integer> ids);

    Optional<Author> getAuthorByName(String name);

    List<Author> getAuthorsByUser(User user);

    Author getAuthorFromDto(AuthorDto authorDto);
}
