package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.config.JwtTokenUtil;
import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.model.dto.AuthorDto;
import com.example.HomeLibrarySpringBoot.service.AuthorService;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/authors")
    @ResponseBody
    public List<Author> authorsList(HttpServletRequest request){
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        return authorService.getAuthorsByUser(user);
    }


    @GetMapping("/showAuthorsBooks/{authorId}")
    public List<Book> showBooksByAuthor(@PathVariable(value = "authorId") Integer id,
                                        HttpServletRequest request){
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        List<Book> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        Author author = authorService.getAuthor(id);
        List<Book> authorsBooks=bookService.getBookByAuthor(author.getName());
        books=usersLibrary.getBooksAndLocation().keySet().stream().filter(authorsBooks::contains).collect(Collectors.toList());
        return books;
    }

    @PostMapping("/updateAuthor")
    public HttpStatus updateAuthor(@RequestBody AuthorDto authorDto){
        Author author = authorService.getAuthorFromDto(authorDto);
        authorService.updateAuthor(author);
        return HttpStatus.ACCEPTED;
    }


}
