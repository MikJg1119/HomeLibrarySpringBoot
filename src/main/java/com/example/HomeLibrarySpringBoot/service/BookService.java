package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;

import java.util.List;

public interface BookService {

    void addBook(Book book);
    void removeBook(Book book);
    Book getBook(int id);
    void updateBook(Book book);
    List<Book> getBooks();
}
