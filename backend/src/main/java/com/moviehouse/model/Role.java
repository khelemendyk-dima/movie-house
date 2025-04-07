package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_ROLE;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = EMPTY_ROLE)
    @Column(name = "name", unique = true)
    private String name;
}
