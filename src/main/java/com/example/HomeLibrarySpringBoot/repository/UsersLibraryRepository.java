package com.example.HomeLibrarySpringBoot.repository;

import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersLibraryRepository extends JpaRepository<UsersLibrary,Integer> {

    UsersLibrary findByUser(User user);
}
