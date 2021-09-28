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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
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
    public String booksList(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        List<Author> authors =authorService.getAuthorsByUser(user);
        model.addAttribute("authors", authors);
        return "authors_list";
    }

    @GetMapping("/showFormForAuthorUpdate/{id}")
    public String showFormForAuthorUpdate(@PathVariable(value = "id") int id, Model model){
        Author author = authorService.getAuthor(id);
        model.addAttribute("author", author);
        return "update_author";

    }

    @GetMapping("/showAuthorsBooks/{id}")
    public String showBooksByAuthor(@PathVariable(value = "id") int id, Model model){
        List<Book> booksToBeLoaned=new ArrayList<Book>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        List<Book> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        Author author = authorService.getAuthor(id);
        List<Book> authorsBooks=bookService.getBookByAuthor(author.getName());
        books=usersLibrary.getBooks().stream().filter(e ->authorsBooks.contains(e)).collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("usersLibrary", usersLibrary);
        model.addAttribute("booksToBeLoaned", booksToBeLoaned);
        return "index";
    }

    @PostMapping("/updateAuthor")
    public String updateAuthor(@ModelAttribute Author author){
        authorService.updateAuthor(author);
        return "redirect:/authors";
    }

//    @GetMapping("/deleteAuthor/{id}") //this method is pretty useless for endusers - it'd need to delete all books by said author
//    public String deleteBook(@PathVariable(value = "id") int id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userService.getUserByEmail(username);
//        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
//
//        authorService.removeAuthor(id);
//        return "redirect:/authors";
//    }

}
