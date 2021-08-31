package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Author;

import java.util.List;

public interface AuthorService {
    void addAuthor(Author author);
    void removeAuthor(int id);
    Author getAuthor(int id);
    void updateAuthor(Author author);
    List<Author> getAuthors();
    List<Author> getAuthorsById(Iterable<Integer> ids);
    Author getAuthorByName(String name);
}
