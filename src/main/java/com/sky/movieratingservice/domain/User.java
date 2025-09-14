package com.sky.movieratingservice.domain;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class User {
    private final String id;
    private String email;
    private String password;
    private Set<Role> roles;

    /**
     * Constructor for creating a new user.
     *
     * @param id Unique identifier for the user
     * @param email User email
     * @param password User password
     */
    public User(String id, String email, String password) {
        this(id, email, password, new HashSet<>());
        // By default, add USER role
//        this.roles.add(Role.USER);
    }

    /**
     * Constructor for creating a new user with specified roles.
     *
     * @param id Unique identifier for the user
     * @param email User email
     * @param password User password
     * @param roles Set of roles for the user
     */
    public User(String id, String email, String password, Set<Role> roles) {
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
        this.roles = roles != null ? roles : new HashSet<>();
    }


    // Custom setter with validation for email
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        this.email = email;
    }

    // Custom setter with validation for password
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }
        this.password = password;
    }

    /**
     * Add a role to the user.
     *
     * @param role The role to add
     */
    public void addRole(Role role) {
        if (role != null) {
            if (this.roles == null) {
                this.roles = new HashSet<>();
            }
            this.roles.add(role);
        }
    }

    /**
     * Remove a role from the user.
     *
     * @param role The role to remove
     */
    public void removeRole(Role role) {
        if (role != null && this.roles != null) {
            this.roles.remove(role);
        }
    }

    /**
     * Check if the user has a specific role.
     *
     * @param role The role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        return role != null && this.roles != null && this.roles.contains(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", roles=" + roles +
                '}';
    }
}
