package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.service.AuthorService;
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

import java.util.List;

@Controller
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    UsersLibraryService usersLibraryService;

    @Autowired
    UserService userService;

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

    @PostMapping("/updateAuthor")
    public String updateAuthor(@ModelAttribute Author author){
        authorService.updateAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/deleteAuthor/{id}")
    public String deleteBook(@PathVariable(value = "id") int id){
        authorService.removeAuthor(id);
        return "redirect:/authors";
    }

}
