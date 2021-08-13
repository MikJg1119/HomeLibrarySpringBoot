package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/authors")
    public String booksList(Model model){
        List<Author> authors =authorService.getAuthors();
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

}
