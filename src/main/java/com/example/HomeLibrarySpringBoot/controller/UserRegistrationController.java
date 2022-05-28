package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.controller.dto.UserRegistrationDto;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/registration")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UsersLibraryService usersLibraryService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

//    @GetMapping
//    public String showRegistrationForm() {
//        return "registration";
//    }

    @PostMapping
    public HttpStatus registerUserAccount(@RequestBody UserRegistrationDto registrationDto) {
        User user = userService.save(registrationDto);
        if (user ==null){
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.ACCEPTED;
    }

}
