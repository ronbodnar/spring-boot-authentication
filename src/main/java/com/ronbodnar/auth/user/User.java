package com.ronbodnar.auth.user;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronbodnar.auth.role.Role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the system.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructs a new User with the specified email and password.
     * The username is set to be the same as the email.
     *
     * @param email    the email address of the user
     * @param password the password for the user
     */
    public User(String email, String password) {
        this.email = email;
        this.username = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User(id=%s, email=%s, password=%s)", id, email, password);
    }
}
