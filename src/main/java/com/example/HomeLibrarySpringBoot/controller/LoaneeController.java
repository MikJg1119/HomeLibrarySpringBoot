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

    @PostMapping("/loanedBooksListByLoanee/{id}")
    public String listOfBooksLoanedByLoanee(@PathVariable(value = "id") int id, Model model){
        model.addAttribute("loanee", loaneeService.getLoanee(id).getName());
        model.addAttribute("loanedBooks", loaneeService.getLoanee(id).getLoanedBooks());
        return "/books";
    }

    @PostMapping("/returnBook/{id}")
    public String returnBookToLibrary(@PathVariable(value = "id") int bookId, Model model){
        Book book = bookService.getBook(bookId);
        Loanee loanee = book.getLoanee();
        loanee.returnLoanedBook(book);
        model.addAttribute("loanee", loanee.getName());
        model.addAttribute("loanedBooks", loanee.getLoanedBooks());
        return "/books";
    }

    @GetMapping ("/loanees")
    public String getLoaneesList(Model model){
        model.addAttribute("loanees",loaneeService.getLoanees());
        return "/loanees_list";
    }

}
