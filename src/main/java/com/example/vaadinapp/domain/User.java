package com.example.vaadinapp.domain;

import lombok.Data;


import java.time.LocalDate;

@Data

public class User {

    private Long id;
    private String name;
    private String surname;
    private String country;
    private LocalDate dateOfBirth;
    private int height;
    private double weight;
}
