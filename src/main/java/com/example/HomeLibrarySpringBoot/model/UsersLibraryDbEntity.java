package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class UsersLibraryDbEntity {

    @Id
    private int userId;

    private String personalBookshelfIdsJson;

    private String personalLoaneesIdsJson;

    private String personalMapBookIdToLoaneeIdJson;


}
