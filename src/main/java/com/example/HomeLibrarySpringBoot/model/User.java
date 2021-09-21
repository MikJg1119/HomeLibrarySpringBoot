package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    private String password;

    public User() {
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;



    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn( name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookId", referencedColumnName = "id")
    )
    private List<Book> books;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn( name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "loaneeId", referencedColumnName = "id")
    )
    private List<Loanee> loanees;

    public User(String name, String email, String password, Collection<Role> roles, List<Book> books, List<Loanee> loanees) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.books = books;
        this.loanees = loanees;
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
