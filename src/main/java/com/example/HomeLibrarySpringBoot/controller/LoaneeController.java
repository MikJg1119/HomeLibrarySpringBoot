package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.config.JwtTokenUtil;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.LoaneeService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
public class LoaneeController {

    @Autowired
    private LoaneeService loaneeService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersLibraryService usersLibraryService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public LoaneeController() {
    }

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
    public HttpStatus loanBooksToLoanee(@PathVariable(value = "loaneeId") Integer loaneeId,
                                        HttpServletRequest request,
                                        @RequestBody Integer [] booksToBeLoanedId){
        List<Book> booksToBeLoaned = new ArrayList<>();
        for (int id : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(id));
        }
        loaneeService.loanBook(booksToBeLoaned,loaneeId);
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        User user = userService.getUserByEmail(username);
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
                                             HttpServletRequest request,
                                         @RequestBody Integer [] booksToBeLoanedId){

        if (!loaneeService.getLoanees().contains(loanee)){
            loaneeService.addLoanee(loanee);
        }

        List<Book> booksToBeLoaned = new ArrayList<>();
        for (int id : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(id));
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getLoanees().add(loanee);

        for (Book book : booksToBeLoaned){
            usersLibrary.getBooksLoanedToLoanees().put(book,loanee);
        }
        loaneeService.loanBook(booksToBeLoaned,loanee.getId());

        return HttpStatus.OK;
    }

    @PostMapping("/loanedBooksListByLoanee/{id}")
    public List<Book> listOfBooksLoanedByLoanee(@PathVariable(value = "id") Integer id,
                                                HttpServletRequest request){
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        User user = userService.getUserByEmail(username);
        List<Book> usersBooks= usersLibraryService.getBooksByUser(user);
        return  loaneeService.getLoanee(id).getLoanedBooks().stream().filter(e ->usersBooks.contains(e)).collect(Collectors.toList());
    }

    @PostMapping("/returnBook/{id}")
    public HttpStatus returnBookToLibrary(@PathVariable(value = "id") Integer bookId,
                                          HttpServletRequest request){
        Book book = bookService.getBook(bookId);
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        User user = userService.getUserByEmail(username);
        Loanee loanee = usersLibraryService.getUsersLibraryByUser(user).checkIfBookIsLoaned(book);
        loanee.returnLoanedBook(book);

        return HttpStatus.ACCEPTED;
    }

    @GetMapping ("/loanees")
    public List<Loanee> getLoaneesList(HttpServletRequest request){
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        User user = userService.getUserByEmail(username);
        return usersLibraryService.getUsersLibraryByUser(user).getLoanees();
    }

}
