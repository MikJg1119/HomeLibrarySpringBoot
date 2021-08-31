package com.example.HomeLibrarySpringBoot.repository;

import com.example.HomeLibrarySpringBoot.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByTitle(String title);
    @Query("select b from Book b where b.author = ?1")
    List<Book> findByAuthor(String author);
    Optional<Book> findByIsbn(String isbn);
}
