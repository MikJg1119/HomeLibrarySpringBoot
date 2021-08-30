package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository authorRepository;



    @Override
    public void addAuthor(Author author) {
    authorRepository.save(author);
    }

    @Override
    public void removeAuthor(int id) {
    authorRepository.deleteById(id);
    }

    @Override
    public Author getAuthor(int id) {
        return authorRepository.getById(id);
    }

    @Override
    public void updateAuthor(Author author) {
    authorRepository.save(author);
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAuthorsById(Iterable<Integer> ids) {
        return authorRepository.findAllById(ids);
    }

    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }
}
