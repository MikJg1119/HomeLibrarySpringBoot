package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.service.LoaneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoaneeController {

    @Autowired
    LoaneeService loaneeService;


}
