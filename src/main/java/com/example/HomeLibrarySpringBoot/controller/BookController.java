package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BookController {

//    @Autowired
    private BookService bookService;

//    @Autowired
    private UserService userService;

//    @Autowired
    private UsersLibraryService usersLibraryService;


    @Autowired
    public BookController(BookService bookService, UserService userService, UsersLibraryService usersLibraryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.usersLibraryService = usersLibraryService;
    }

    @GetMapping("/booksList")
    @ResponseBody
    public List<Book> booksList(@RequestParam @Nullable String email,
                                @RequestParam @Nullable int userId){
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        List<Book> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        books=usersLibrary.getBooks();

        return books;
    }


    @GetMapping("/addBook")
    public String addBook(Model model){
        String isbn = "";
        model.addAttribute("isbn", isbn);
        return "addBook";
    }

    @PostMapping("/saveBook")
    public HttpStatus saveBook(@RequestParam String isbn,
                                @RequestParam @Nullable String email,
                               @RequestParam @Nullable int userId){
        Book book = bookService.getBookByIsbn(isbn);
        if (book.getTitle().equals("")){

            return HttpStatus.NOT_FOUND;
        }
        User user = userService.getUserByEmail(email);
        if (email == null){
            user = userService.getUserById(userId);
        }
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
       usersLibrary.getBooks().add(book);
        usersLibraryService.save(usersLibrary);
        return HttpStatus.OK;
    }

    @PostMapping("/updateBook")
    public HttpStatus updateBook(@RequestBody Book book){
        bookService.updateBook(book);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getBooks().add(book.getId(),book);
        return HttpStatus.ACCEPTED;
    }

//    @GetMapping("/showFormForUpdate/{id}")
//    public HttpStatus showFormForUpdate(@PathVariable(value = "id") int id, Model model){
//        Book book = bookService.getBook(id);
//        model.addAttribute("book", book);
//        return HttpStatus.ACCEPTED;
//
//    }

    @DeleteMapping("/deleteBook/{id}")
    public HttpStatus deleteBook(@PathVariable(value = "id") int id,
                                    @RequestParam int userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getBooks().remove(bookService.getBook(id));
        usersLibraryService.save(usersLibrary);


        return HttpStatus.ACCEPTED;
    }



}
