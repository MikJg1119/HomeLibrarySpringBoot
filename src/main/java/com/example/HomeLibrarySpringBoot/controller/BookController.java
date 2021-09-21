package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
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

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String booksList(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username);
        List<Book> books;
        try {
            books=user.getBooks();
        }catch (NullPointerException e){
            books=new ArrayList<Book>();
            return "redirect:/addBook";
        }

        model.addAttribute("books", books);

        List<Loanee> loanees = user.getLoanees();

        return "index";
    }

    @GetMapping("/addBook")
    public String addBook(Model model){
        String isbn = "";
        model.addAttribute("isbn", isbn);
        return "addBook";
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("isbn") String isbn){
        Book book = bookService.getBookByIsbn(isbn);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username);
        user.getBooks().add(book);
        return "redirect:/";
    }

    @PostMapping("/updateBook") // different handling method for update, not to overwrite the data with new ISBN search
    public String updateBook(@ModelAttribute("book") Book book){
        bookService.updateBook(book);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model){
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "update_book";

    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(value = "id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username);
        user.getBooks().remove(bookService.getBook(id));

        return "redirect:/";
    }



}
