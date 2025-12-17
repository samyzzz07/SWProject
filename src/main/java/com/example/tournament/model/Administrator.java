package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

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
     * Create a new tournament.
     */
    public void createTournament() {
        System.out.println("Administrator " + getUsername() + " is creating a tournament.");
    }
    
    /**
     * Define tournament rules.
     */
    public void defineTournamentRules() {
        System.out.println("Administrator " + getUsername() + " is defining tournament rules.");
    }
    
    /**
     * View tournament report.
     */
    public void viewTournamentReport() {
        System.out.println("Administrator " + getUsername() + " is viewing tournament report.");
    }
    
    /**
     * End a tournament.
     */
    public void endTournament() {
        System.out.println("Administrator " + getUsername() + " is ending tournament.");
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
