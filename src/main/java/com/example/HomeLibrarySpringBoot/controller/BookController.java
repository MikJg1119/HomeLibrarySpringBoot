package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.config.JwtTokenUtil;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersLibraryService usersLibraryService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public BookController() {
    }

    public BookController(BookService bookService, UserService userService, UsersLibraryService usersLibraryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.usersLibraryService = usersLibraryService;
    }

    @GetMapping(value = "/booksList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Book> booksList(HttpServletRequest request){
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
        List<Book> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        books=usersLibrary.getBooks();

        return books;
    }


    @PostMapping(value = "/saveBook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus saveBook(@RequestHeader String isbn,
                               HttpServletRequest request){
        Book book = bookService.getBookByIsbn(isbn);
        if (book.getTitle().equals("")){

            return HttpStatus.NOT_FOUND;
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
       usersLibrary.getBooks().add(book);
        usersLibraryService.save(usersLibrary);
        return HttpStatus.OK;
    }

    @PostMapping("/updateBook")
    public HttpStatus updateBook(@RequestBody Book book,
                                 HttpServletRequest request){
        bookService.updateBook(book);
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
        usersLibrary.getBooks().add(book.getId(),book);
        return HttpStatus.ACCEPTED;
    }


    @DeleteMapping("/deleteBook/{id}")
    public HttpStatus deleteBook(@PathVariable(value = "id") int id,
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
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getBooks().remove(bookService.getBook(id));
        usersLibraryService.save(usersLibrary);

        return HttpStatus.ACCEPTED;
    }



}
