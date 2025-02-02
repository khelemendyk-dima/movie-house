package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;
import static com.moviehouse.util.constant.RegexConstant.NAME_REGEX;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_ROLE)
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Pattern(regexp = NAME_REGEX, message = INCORRECT_NAME_FORMAT)
    @NotBlank(message = EMPTY_NAME)
    @Size(max = 50, message = NAME_SIZE_EXCEEDED)
    private String name;

    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
