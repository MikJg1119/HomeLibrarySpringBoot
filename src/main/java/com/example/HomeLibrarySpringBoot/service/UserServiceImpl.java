package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.dto.UserRegistrationDto;
import com.example.HomeLibrarySpringBoot.model.Role;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersLibraryService usersLibraryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
        userRepository.save(user);
        UsersLibrary usersLibrary = new UsersLibrary(user);
        usersLibraryService.save(usersLibrary);
//            user.setLoanees(new ArrayList<Loanee>());
//            user.setBooks(new ArrayList<Book>());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void updateUser(User user){
        userRepository.deleteById(user.getId());
        userRepository.save(user);
    }
    @Override
    public User getUserByName(String name){
        User user;
        user = userRepository.findByName(name);

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    //    @Override
//    public void addBookToUser(User user, Book book) {
//       try {
//           user.getBooks().add(book);
//       }catch (NullPointerException e){
//           this.setBooks(user, new ArrayList<Book>());
//           userRepository.save(user);
//           user.getBooks().add(book);
//           userRepository.save(user);
//       }
//
//    }
//
//    @Override
//    public void setBooks(User user, List<Book> books) {
//        user.setBooks(books);
////        this.updateUser(user);
//    }
//
//    @Override
//    public void setLoanees(User user, List<Loanee> loanees) {
//        user.setLoanees(loanees);
////        this.updateUser(user);
//    }
}
