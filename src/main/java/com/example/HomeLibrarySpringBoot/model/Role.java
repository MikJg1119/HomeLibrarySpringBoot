package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    public Role() {
    }

    @Autowired
    public Role(String name) {
        super();
        this.name = name;
    }
}
