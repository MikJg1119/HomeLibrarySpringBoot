package com.example.HomeLibrarySpringBoot.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {

    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    @JoinColumn(name="book_id", referencedColumnName = "id")
    private Set<Book> booksByAuthor;

    @Autowired
    public Author() {
        this.booksByAuthor=new HashSet<Book>();
    }

    public Author(String author) {
        this.name = author;
        this.booksByAuthor=new HashSet<Book>();
    }


    @Override
    public String toString() {
        return this.name;
    }
}
