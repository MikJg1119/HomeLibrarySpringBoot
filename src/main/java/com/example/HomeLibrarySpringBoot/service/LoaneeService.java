package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;

import java.security.spec.InvalidParameterSpecException;
import java.util.List;

public interface LoaneeService {
    void addLoanee(Loanee loanee);

    void removeLoanee(int id);

    Loanee getLoanee(int id);

    List<Loanee> getLoanees();

    void loanBook(Book book, int loaneeId);

    void loanBook(UsersLibrary usersLibrary, User user, int [] booksToBeLoaned, int loaneeId) throws InvalidParameterSpecException;

    void returnLoanedBook(Book book, int loaneeId);

    void returnLoanedBook(List<Book> booksToBeReturned, int loaneeId);

    List<Loanee> getAllLoaneesById(Iterable<Integer> ids);
}
