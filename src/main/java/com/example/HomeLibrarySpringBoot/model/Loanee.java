package com.example.HomeLibrarySpringBoot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Loanee {

    @Id
    @Column(columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
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

    @Autowired
    public Loanee(String name) {
        this.name = name;
        loanedBooks = new ArrayList<Book>();
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
