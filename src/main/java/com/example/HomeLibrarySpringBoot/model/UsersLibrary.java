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
public class UsersLibrary {

    @Id
    @Column(columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn( name = "userLibraryId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookId", referencedColumnName = "id")
    )
    private List<Book> books = new ArrayList<Book>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn( name = "userLibraryId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "loaneeId", referencedColumnName = "id")
    )
    private List<Loanee> loanees = new ArrayList<Loanee>();

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
        for (Loanee loanee : loanees){
            if (loanee.getLoanedBooks().contains(book)){
                return loanee;
            }
        }
        return null;
    }
}
