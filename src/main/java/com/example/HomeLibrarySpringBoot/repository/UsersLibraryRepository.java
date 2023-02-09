package com.example.HomeLibrarySpringBoot.repository;

import com.example.HomeLibrarySpringBoot.model.UsersLibraryDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersLibraryRepository extends JpaRepository<UsersLibraryDbEntity,Integer> {

}
