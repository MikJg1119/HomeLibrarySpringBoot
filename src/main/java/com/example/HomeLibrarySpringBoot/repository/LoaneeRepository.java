package com.example.HomeLibrarySpringBoot.repository;

import com.example.HomeLibrarySpringBoot.model.Loanee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoaneeRepository extends JpaRepository<Loanee,Integer> {

}
