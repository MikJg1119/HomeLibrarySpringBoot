package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Loanee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Getter
    @Setter
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "loanees_books",
            joinColumns = @JoinColumn(
                    name = "loanee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"))
    private List<Book> loanedBooks;

    public Loanee(String name) {
        this.name = name;
    }

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
