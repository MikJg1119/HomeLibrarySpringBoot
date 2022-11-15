package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.service.AuthorService;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    UsersLibraryService usersLibraryService;

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @GetMapping("/authors")
    @ResponseBody
    public List<Author> authorsList(@RequestParam @Nullable String email,
                                    @RequestParam @Nullable Integer userId){
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        return authorService.getAuthorsByUser(user);
    }


    @GetMapping("/showAuthorsBooks/{authorId}")
    public List<Book> showBooksByAuthor(@PathVariable(value = "authorId") Integer id,
                                    @RequestParam @Nullable String email,
                                    @RequestParam @Nullable int userId){
        User user = email !=null ? userService.getUserByEmail(email) : userService.getUserById(userId);
        List<Book> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        Author author = authorService.getAuthor(id);
        List<Book> authorsBooks=bookService.getBookByAuthor(author.getName());
        books=usersLibrary.getBooks().stream().filter(authorsBooks::contains).collect(Collectors.toList());
        return books;
    }

    @PostMapping("/updateAuthor")
    public HttpStatus updateAuthor(@RequestBody Author author){
        authorService.updateAuthor(author);
        return HttpStatus.ACCEPTED;
    }


}
