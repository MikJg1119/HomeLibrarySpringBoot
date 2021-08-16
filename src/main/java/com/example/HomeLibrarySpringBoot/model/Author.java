package com.example.HomeLibrarySpringBoot.model;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "authors")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "authorid"))
})
public class Author {

    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    @JoinColumn(name="authorid")
    private Set<Book> booksByAuthor;

    @Autowired
    public Author() {
    }

    public Author(String author) {
        this.name = author;

    }


    @Override
    public String toString() {
        return this.name;
    }
}
