package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.*;
import com.example.HomeLibrarySpringBoot.repository.UsersLibraryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersLibraryServiceImpl implements UsersLibraryService {

    private UsersLibraryRepository usersLibraryRepository;

    private UserService userService;

    private LoaneeService loaneeService;

    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setUsersLibraryRepository(UsersLibraryRepository usersLibraryRepository) {
        this.usersLibraryRepository = usersLibraryRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLoaneeService(LoaneeService loaneeService) {
        this.loaneeService = loaneeService;
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public UsersLibrary getUsersLibraryByUser(User user) {
        Optional<UsersLibraryDbEntity> dbRecord = usersLibraryRepository.findById(user.getId());
        return dbRecord.map(this::toFinalEntity).orElse(null);
    }

    @Override
    public void save(UsersLibrary usersLibrary) {
        UsersLibraryDbEntity usersLibraryDbEntity = toDbEntity(usersLibrary);
        usersLibraryRepository.save(usersLibraryDbEntity);
    }

    @Override
    public void setBooks(User user, List<Book> books) {
        Optional<UsersLibraryDbEntity> userDb = usersLibraryRepository.findById(user.getId());
        if (userDb.isPresent()){
            UsersLibraryDbEntity usersLibraryDbEntity = userDb.get();
            List<Integer> bookIds = null;
            try {
                bookIds = objectMapper.readValue(userDb.get().getPersonalBookshelfIdsJson(), List.class);
                for(Book book : books){
                    if (!bookIds.contains(book.getId())){
                        bookIds.add(book.getId());
                    }
                }
                usersLibraryDbEntity.setPersonalBookshelfIdsJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookIds));
                usersLibraryRepository.save(usersLibraryDbEntity);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void setLoanees(User user, List<Loanee> loanees) {
        Optional<UsersLibraryDbEntity> userDb = usersLibraryRepository.findById(user.getId());
        if (userDb.isPresent()){
            UsersLibraryDbEntity usersLibraryDbEntity = userDb.get();
            List<Integer> loaneeIds = null;
            try {
                loaneeIds = objectMapper.readValue(userDb.get().getPersonalLoaneesIdsJson(), List.class);
                for(Loanee loanee : loanees){
                    if (!loaneeIds.contains(loanee.getId())){
                        loaneeIds.add(loanee.getId());
                    }
                }
                usersLibraryDbEntity.setPersonalLoaneesIdsJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loaneeIds));
                usersLibraryRepository.save(usersLibraryDbEntity);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public List<Book> getBooksByUser(User user) {

        Optional<UsersLibraryDbEntity> dbRecord = usersLibraryRepository.findById(user.getId());
        return dbRecord.map(usersLibraryDbEntity -> toFinalEntity(usersLibraryDbEntity).getBooks()).orElse(null);
    }

    @Override
    public List<Loanee> getLoaneesByUser(User user) {
        Optional<UsersLibraryDbEntity> dbRecord = usersLibraryRepository.findById(user.getId());
        return dbRecord.map(usersLibraryDbEntity -> toFinalEntity(usersLibraryDbEntity).getLoanees()).orElse(null);
    }

    @Override
    public List<Book> getBooksLoanedToLoaneeByUser(int userId, int loaneeId) {
        Optional<UsersLibraryDbEntity> dbRecord = usersLibraryRepository.findById(userId);
        List<Book> booksLoanedToLoanee = new ArrayList<>();
        if (dbRecord.isPresent()){
            UsersLibrary usersLibrary = toFinalEntity(dbRecord.get());
            for (Book book : usersLibrary.getBooksLoanedToLoanees().keySet()){
                if ((usersLibrary.getBooksLoanedToLoanees().get(book).getId() == loaneeId)) {
                    booksLoanedToLoanee.add(book);
                }

            }
        }
        return booksLoanedToLoanee;
    }

    private UsersLibraryDbEntity toDbEntity(UsersLibrary usersLibrary){


        UsersLibraryDbEntity usersLibraryDbEntity = new UsersLibraryDbEntity();
        usersLibraryDbEntity.setUserId(usersLibrary.getUser().getId());
        Map<Integer, Integer> bookIdsToLoaneeIds = new HashMap<>();
        List<Integer> bookIds = new ArrayList<>();
        for (Book book : usersLibrary.getBooks()){
            bookIds.add(book.getId());
            if (usersLibrary.getBooksLoanedToLoanees().containsKey(book)){
                bookIdsToLoaneeIds.put(book.getId(), usersLibrary.getBooksLoanedToLoanees().get(book).getId());
            }
        }
        List<Integer> loaneeIds = new ArrayList<>();
        for (Loanee loanee : usersLibrary.getLoanees()){
            loaneeIds.add(loanee.getId());
        }

        try {
            usersLibraryDbEntity.setPersonalBookshelfIdsJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookIds));
            usersLibraryDbEntity.setPersonalLoaneesIdsJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loaneeIds));
            usersLibraryDbEntity.setPersonalMapBookIdToLoaneeIdJson(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookIdsToLoaneeIds));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return usersLibraryDbEntity;
    }

    private UsersLibrary toFinalEntity (UsersLibraryDbEntity usersLibraryDbEntity)  {
        UsersLibrary usersLibrary = new UsersLibrary();
        usersLibrary.setUser(userService.getUserById(usersLibraryDbEntity.getUserId()));
        List<Integer> booksIds;
        List<Integer> loaneeIds;
        Map<Integer, Integer> booksToLoaneesIds;
        try {
            booksIds = objectMapper.readValue(usersLibraryDbEntity.getPersonalBookshelfIdsJson(), List.class);
            loaneeIds = objectMapper.readValue(usersLibraryDbEntity.getPersonalLoaneesIdsJson(), List.class);
            booksToLoaneesIds = objectMapper.readValue(usersLibraryDbEntity.getPersonalMapBookIdToLoaneeIdJson(), Map.class);
            usersLibrary.setBooks(bookService.getBooksById(booksIds));
            usersLibrary.setLoanees(loaneeService.getAllLoaneesById(loaneeIds));
            Map<Book, Loanee> booksLoanedToLoanee = new HashMap<>();
            List <Book> loanedBooks = bookService.getBooksById(booksToLoaneesIds.keySet());
            for (Book book : loanedBooks){
                Loanee loanee = loaneeService.getLoanee(booksToLoaneesIds.get(book.getId()));
                booksLoanedToLoanee.put(book, loanee);
            }
            usersLibrary.setBooksLoanedToLoanees(booksLoanedToLoanee);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return usersLibrary;

    }
}
