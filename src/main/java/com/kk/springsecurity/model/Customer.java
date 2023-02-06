package com.kk.springsecurity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    // the generator is native and is refering to GenericGenerator with name native
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // strategy native means sequence generation is depended on underlying database
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String email;
    private String pwd;
    private String role;
}
