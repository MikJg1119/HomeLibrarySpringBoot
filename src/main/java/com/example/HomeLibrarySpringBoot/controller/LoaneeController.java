package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.LoaneeService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
public class LoaneeController {

    private LoaneeService loaneeService;
    private BookService bookService;
    private UserService userService;
    private UsersLibraryService usersLibraryService;



    @Autowired
    public LoaneeController(LoaneeService loaneeService, BookService bookService, UserService userService, UsersLibraryService usersLibraryService) {
        this.loaneeService = loaneeService;
        this.bookService = bookService;
        this.userService = userService;
        this.usersLibraryService = usersLibraryService;
    }

//    @PostMapping("/loanBooksForm")
//    public HttpStatus loanBooks(@RequestParam @Nullable String email,
//                                @RequestParam @Nullable int userId,
//                                @RequestBody int [] booksToBeLoanedId){
//
//        if (booksToBeLoanedId.length==0){
//            return HttpStatus.NO_CONTENT;
//        }
//        List<Book> booksToBeLoaned=new ArrayList<Book>();
//        for(int i : booksToBeLoanedId){
//            booksToBeLoaned.add(bookService.getBook(i));
//
//        }
//        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
//        return "/loanBooks";
//    }
    @PostMapping("/loanBooksToLoanee/{loaneeId}")
    public HttpStatus loanBooksToLoanee(@PathVariable(value = "loaneeId") int loaneeId,
                                        @RequestParam @Nullable String email,
                                        @RequestParam @Nullable int userId,
                                        @RequestBody int [] booksToBeLoanedId){
        List<Book> booksToBeLoaned = new ArrayList<>();
        for (int id : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(id));
        }
        loaneeService.loanBook(booksToBeLoaned,loaneeId);
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        List<Loanee> usersLoanees = usersLibrary.getLoanees();
        Loanee loanee = loaneeService.getLoanee(loaneeId);
        for (Book book : booksToBeLoaned){
            usersLibrary.getBooksLoanedToLoanees().put(book,loanee);
        }

        if(!usersLoanees.contains(loanee)){
            usersLoanees.add(loanee);
        }
        usersLibraryService.save(usersLibrary);

        return HttpStatus.OK;
    }

    @PostMapping("/saveLoaneeAndLoanBooks")
    public HttpStatus saveLoaneeAndLoanBooks(@RequestBody Loanee loanee,
                                        @RequestParam @Nullable String email,
                                         @RequestParam @Nullable int userId,
                                         @RequestBody int [] booksToBeLoanedId){

        if (!loaneeService.getLoanees().contains(loanee)){
            loaneeService.addLoanee(loanee);
        }

        List<Book> booksToBeLoaned = new ArrayList<>();
        for (int id : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(id));
        }

        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getLoanees().add(loanee);

        for (Book book : booksToBeLoaned){
            usersLibrary.getBooksLoanedToLoanees().put(book,loanee);
        }
        loaneeService.loanBook(booksToBeLoaned,loanee.getId());

        return HttpStatus.OK;
    }

    @PostMapping("/loanedBooksListByLoanee/{id}")
    public List<Book> listOfBooksLoanedByLoanee(@PathVariable(value = "id") int id,
                                                @RequestParam @Nullable String email,
                                                @RequestParam @Nullable int userId){
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        List<Book> usersBooks= usersLibraryService.getBooksByUser(user);
        return  loaneeService.getLoanee(id).getLoanedBooks().stream().filter(e ->usersBooks.contains(e)).collect(Collectors.toList());
    }

    @PostMapping("/returnBook/{id}")
    public HttpStatus returnBookToLibrary(@PathVariable(value = "id") int bookId,
                                      @RequestParam @Nullable String email,
                                      @RequestParam @Nullable int userId){
        Book book = bookService.getBook(bookId);
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        Loanee loanee = usersLibraryService.getUsersLibraryByUser(user).checkIfBookIsLoaned(book);
        loanee.returnLoanedBook(book);

        return HttpStatus.ACCEPTED;
    }

    @GetMapping ("/loanees")
    public List<Loanee> getLoaneesList(@RequestParam @Nullable String email,
                                       @RequestParam @Nullable int userId){
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        return usersLibraryService.getUsersLibraryByUser(user).getLoanees();
    }

}
