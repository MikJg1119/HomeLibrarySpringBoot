package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.repository.UsersLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersLibraryServiceImpl implements UsersLibraryService {

    @Autowired
    UsersLibraryRepository usersLibraryRepository;

    @Override
    public UsersLibrary getUsersLibraryByUser(User user) {
        return usersLibraryRepository.findByUser(user);
    }

    @Override
    public void save(UsersLibrary usersLibrary) {
        usersLibraryRepository.save(usersLibrary);
    }

    @Override
    public void setBooks(User user, List<Book> books) {
       UsersLibrary userDb = usersLibraryRepository.findByUser(user);
       userDb.setBooks(books);
       usersLibraryRepository.save(userDb);
    }

    @Override
    public void setLoanees(User user, List<Loanee> loanees) {
        UsersLibrary userDb = usersLibraryRepository.findByUser(user);
        userDb.setLoanees(loanees);
        usersLibraryRepository.save(userDb);
    }

    @Override
    public List<Book> getBooksByUser(User user) {

        return usersLibraryRepository.findByUser(user).getBooks();
    }

    @Override
    public List<Loanee> getLoaneesByUser(User user) {
        return usersLibraryRepository.findByUser(user).getLoanees();
    }
}
