package com.ronbodnar.auth.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private int id;

    private String email;
    private String password;

    @Override
    public String toString() {
        return String.format("User(id=%s, email=%s, password=%s)",
                id, email, password);
    }
}