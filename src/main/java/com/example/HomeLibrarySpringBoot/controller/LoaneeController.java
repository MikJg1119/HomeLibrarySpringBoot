package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.Loanee;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.LoaneeService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoaneeController {

    @Autowired
    LoaneeService loaneeService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    UsersLibraryService usersLibraryService;


    private List<Book> booksToBeLoaned=new ArrayList<Book>();

    @GetMapping("/loanBooksForm")
    public String loanBooks(@RequestParam(value = "loan") int [] booksToBeLoanedId, BindingResult bindingResult, Model model){

        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));


        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        model.addAttribute("booksToBeLoaned",booksToBeLoaned);
        model.addAttribute("loanees",usersLibraryService.getLoaneesByUser(user));
        return "/loanBooks";
    }
    @PostMapping("/loanBooksToLoanee/{id}")
    public String loanBooksToLoanee(@PathVariable(value = "id") int id, @RequestParam(value = "loan") int [] booksToBeLoanedId){
        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));
        }
        loaneeService.loanBook(booksToBeLoaned,id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        List<Loanee> usersLoanees;
        try {
            usersLoanees = usersLibraryService.getUsersLibraryByUser(user).getLoanees();
        }catch (NullPointerException e){
            usersLoanees = new ArrayList<Loanee>();
            usersLibraryService.getUsersLibraryByUser(user).setLoanees(usersLoanees);

        }
        Loanee loanee = loaneeService.getLoanee(id);
        if(!usersLoanees.contains(loanee)){
            usersLoanees.add(loanee);
        }
        return "redirect:/";
    }

    @PostMapping("/saveLoaneeAndLoanBooks")
    public String saveLoaneeAndLoanBooks(@RequestParam(value = "loan") int [] booksToBeLoanedId, @ModelAttribute("loanee") Loanee loanee){
        for(int i : booksToBeLoanedId){
            booksToBeLoaned.add(bookService.getBook(i));
        }
        loaneeService.addLoanee(loanee);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        usersLibraryService.getLoaneesByUser(user).add(loanee);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);

        Loanee loanee = usersLibraryService.getUsersLibraryByUser(user).checkIfBookIsLoaned(book);
        loanee.returnLoanedBook(book);
        model.addAttribute("loanee", loanee.getName());
        model.addAttribute("loanedBooks", loanee.getLoanedBooks());
        return "/books";
    }

    @GetMapping ("/loanees")
    public String getLoaneesList(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        model.addAttribute("loanees",usersLibraryService.getUsersLibraryByUser(user).getLoanees());
        return "/loanees_list";
    }

}
