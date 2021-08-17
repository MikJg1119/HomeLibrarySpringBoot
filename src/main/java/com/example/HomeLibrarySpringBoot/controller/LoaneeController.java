package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.LoaneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoaneeController {

    @Autowired
    LoaneeService loaneeService;

    @Autowired
    BookService bookService;

    private List<Book> booksToBeLoaned;

    @GetMapping("/loanBooksForm")
    public String loanBooks(@RequestParam(value = "loan") int [] booksToBeLoanedId, BindingResult bindingResult, Model model){

        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));


        }
        model.addAttribute("booksToBeLoaned",booksToBeLoaned);
        model.addAttribute("loanees",loaneeService.getLoanees());
        return "/loanBooks";
    }
    @PostMapping("/loanBooksToLoanee/{id}")
    public String loanBooksToLoanee(@PathVariable(value = "id") int id, @RequestParam(value = "loan") int [] booksToBeLoanedId){
        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));
        }
        loaneeService.loanBook(booksToBeLoaned,id);
        return "redirect:/";
    }

    @PostMapping("/saveLoaneeAndLoanBooks")
    public String saveLoaneeAndLoanBooks(@RequestParam(value = "loan") int [] booksToBeLoanedId, @ModelAttribute("loanee") Loanee loanee){
        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));
        }
        loaneeService.addLoanee(loanee);
        loaneeService.loanBook(booksToBeLoaned,loanee.getId());

        return "redirect:/";
    }

}
