package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;

import java.util.List;

public interface LoaneeService {
    void addLoanee(Loanee loanee);
    void removeLoanee(int id);
    Loanee getLoanee(int id);
    List<Loanee> getLoanees();
    void loanBook(Book book, int loaneeId);
    void loanBook(List<Book> booksToBeLoaned, int loaneeId);
    void returnLoanedBook(Book book,int loaneeId);
    void returnLoanedBook(List<Book> booksToBeReturned, int loaneeId);
}
