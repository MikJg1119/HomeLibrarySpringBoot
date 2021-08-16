package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Loanee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @OneToMany(mappedBy = "loaneeId")
    private Set<Book> loanedBooks;

    public void addLoanedBook(Book book){
        loanedBooks.add(book);
    }

    public void addLoanedBook(List<Book> booksToBeLoaned){
        loanedBooks.addAll(booksToBeLoaned);
    }

    public void returnLoanedBook(Book book){
        loanedBooks.remove(book);
    }
    public void returnLoanedBook(List<Book> booksToBeReturned){
        for (Book book : booksToBeReturned){
            returnLoanedBook(book);
        }

    }
}
