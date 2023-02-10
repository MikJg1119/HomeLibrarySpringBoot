package com.example.HomeLibrarySpringBoot.model;

import lombok.Getter;
import lombok.Setter;
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


    @OneToOne
    private User user;


    private Map<Book, String> booksAndLocation = new HashMap<>();


    private List<Loanee> loanees = new ArrayList<>();


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
        return booksLoanedToLoanees.get(book);
    }
}
