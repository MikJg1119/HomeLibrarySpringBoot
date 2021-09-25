package com.example.HomeLibrarySpringBoot.service;


import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;

import java.util.List;

public interface UsersLibraryService {

    void save(UsersLibrary usersLibrary);
    UsersLibrary getUsersLibraryByUser(User user);
    void setBooks(User user, List<Book> books);
    void setLoanees(User user,List<Loanee> loanees);
    List<Book> getBooksByUser(User user);
    List<Loanee> getLoaneesByUser(User user);
}
