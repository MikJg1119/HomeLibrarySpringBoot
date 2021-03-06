package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.repository.LoaneeRepository;
import com.example.HomeLibrarySpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoaneeServiceImpl implements LoaneeService{


    @Autowired
    private LoaneeRepository loaneeRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public void addLoanee(Loanee loanee) {
        loaneeRepository.save(loanee);
    }

    @Override
    public void removeLoanee(int id) {
        loaneeRepository.delete(loaneeRepository.getById(id));
    }

    @Override
    public Loanee getLoanee(int id) {
        return loaneeRepository.getById(id);
    }

    @Override
    public List<Loanee> getLoanees() {
        return loaneeRepository.findAll();
    }

    @Override
    public void loanBook(Book book,int loaneeId) {
        loaneeRepository.getById(loaneeId).addLoanedBook(book);
    }

    @Override
    public void loanBook(List<Book> booksToBeLoaned,int loaneeId) {
        loaneeRepository.getById(loaneeId).addLoanedBook(booksToBeLoaned);
    }

    @Override
    public void returnLoanedBook(Book book, int loaneeId) {
        loaneeRepository.getById(loaneeId).returnLoanedBook(book);
    }

    @Override
    public void returnLoanedBook(List<Book> booksToBeReturned, int loaneeId) {
        Loanee loanee = loaneeRepository.getById(loaneeId);
        loanee.setLoanedBooks(loanee.returnLoanedBook(booksToBeReturned));
        loaneeRepository.save(loanee);
//        loaneeRepository.getById(loaneeId).returnLoanedBook(booksToBeReturned);
    }

    @Override
    public List<Loanee> getAllLoaneesById(Iterable<Integer> ids) {
        return loaneeRepository.findAllById(ids);
    }

}
