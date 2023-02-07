package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.dto.BookDto;

import java.util.List;

public interface BookService {

    void addBook(Book book);

    void removeBook(int id);

    Book getBook(int id);

    void updateBook(Book book);

    List<Book> getBooks();

    List<Book> getBooksById(Iterable<Integer> ids);

    Book getBookByName(String title);

    List<Book> getBookByAuthor(String author);

    Book getBookByIsbn(String isbn);

    Book toBook(BookDto bookDto);

    BookDto toBookDto(Book book);

    List<BookDto> getAllBooksDto(List<Book> books);

    Book scrapeBookByIsbn(Book book, String isbn);
}
