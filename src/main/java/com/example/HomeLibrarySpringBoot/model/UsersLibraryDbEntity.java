package com.example.HomeLibrarySpringBoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class UsersLibraryDbEntity {

    @Column(columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private int userId;

    private String personalBookshelfIdsAndLocationJson;

    private String personalLoaneesIdsJson;

    private String personalMapBookIdToLoaneeIdJson;


}
