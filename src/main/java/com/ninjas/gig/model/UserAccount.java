package com.ninjas.gig.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String userName;
    private String password;
    private UserType userType;
}
