package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a NonManager in the tournament system.
 * A user who is not a Game Manager, possibly a team captain or general user.
 */
@Entity
@DiscriminatorValue("NON_MANAGER")
public class NonManager extends User {
    
    public NonManager() {
        super();
    }
    
    public NonManager(String username, String password, String email) {
        super(username, password, email, UserRole.NON_MANAGER);
    }
}
