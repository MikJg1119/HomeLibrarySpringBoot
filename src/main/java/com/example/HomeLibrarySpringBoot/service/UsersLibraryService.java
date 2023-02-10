package com.example.HomeLibrarySpringBoot.service;


import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;

import java.util.List;
import java.util.Map;

public interface UsersLibraryService {

    void save(UsersLibrary usersLibrary);

    UsersLibrary getUsersLibraryByUser(User user);

    void setBooks(User user, Map<Book, String> books);

    void setLoanees(User user, List<Loanee> loanees);

    Map<Book, String> getBooksByUser(User user);

    List<Loanee> getLoaneesByUser(User user);

    List<Book> getBooksLoanedToLoaneeByUser(int userId, int loaneeId);

    void returnBook(Book book, User user);
}
