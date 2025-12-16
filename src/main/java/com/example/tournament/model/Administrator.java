package com.example.tournament.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Entity representing an administrator user.
 */
@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends User {
    
    private String department;
    
    // Constructors
    public Administrator() {
        super();
        setRole(UserRole.ADMINISTRATOR);
    }
    
    public Administrator(String username, String password, String email) {
        super(username, password, email, UserRole.ADMINISTRATOR);
    }
    
    public Administrator(String username, String password, String email, String department) {
        super(username, password, email, UserRole.ADMINISTRATOR);
        this.department = department;
    }
    
    // Getters and Setters
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Placeholder method for managing users.
     */
    public void manageUsers() {
        System.out.println("Administrator " + getUsername() + " is managing users.");
    }
    
    /**
     * Placeholder method for system configuration.
     */
    public void configureSystem() {
        System.out.println("Administrator " + getUsername() + " is configuring the system.");
    }
    
    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
