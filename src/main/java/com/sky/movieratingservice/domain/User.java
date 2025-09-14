package com.sky.movieratingservice.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class User {
    private final String id;
    private String email;
    private String password;
    private List<Role> roles;

    public User(String id, String email, String password) {
        this(id, email, password, new ArrayList<>());
        // By default, add USER role
//        this.roles.add(Role.USER);
    }

    public User(String id, String email, String password, List<Role> roles) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }

        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles != null ? roles : new ArrayList<>();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }
        this.password = password;
    }

    public void addRole(Role role) {
        if (role != null) {
            if (this.roles == null) {
                this.roles = new ArrayList<>();
            }
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (role != null && this.roles != null) {
            this.roles.remove(role);
        }
    }

    public boolean hasRole(Role role) {
        return role != null && this.roles != null && this.roles.contains(role);
    }


}
