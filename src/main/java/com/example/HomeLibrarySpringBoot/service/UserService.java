package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.controller.dto.UserRegistrationDto;
import com.example.HomeLibrarySpringBoot.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
    User getUserByName(String name);

}
