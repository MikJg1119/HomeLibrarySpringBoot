package com.example.HomeLibrarySpringBoot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class UsersLibrary {

    @Id
    @Column(columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User user;


    @JoinColumn
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            joinColumns = @JoinColumn( name = "userLibraryId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookId", referencedColumnName = "id")
    )
    private List<Book> books = new ArrayList<Book>();


    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            joinColumns = @JoinColumn( name = "userLibraryId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "loaneeId", referencedColumnName = "id")
    )
    private List<Loanee> loanees = new ArrayList<Loanee>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "loanee_book_mapping",
            joinColumns = {@JoinColumn(name = "usersLibraryId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "bookId", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "loaneeId")
    private Map<Book,Loanee> booksLoanedToLoanees =new HashMap<>();


    public UsersLibrary() {
//        this.books=new ArrayList<Book>();
//        this.loanees=new ArrayList<Loanee>();
    }

    @Autowired
    public UsersLibrary(User user) {
        this.user = user;
//        this.books=new ArrayList<Book>();
//        this.loanees=new ArrayList<Loanee>();
    }

    public Loanee checkIfBookIsLoaned(Book book){
//        for (Loanee loanee : loanees){
//            if (loanee.getLoanedBooks().contains(book)){
//                return loanee;
//            }
//        }
        booksLoanedToLoanees.get(book);
        return booksLoanedToLoanees.get(book);
    }
}
