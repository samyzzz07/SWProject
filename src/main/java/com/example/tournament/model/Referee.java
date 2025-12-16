package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a Referee in the tournament system.
 * An official responsible for posting game scores.
 */
@Entity
@DiscriminatorValue("REFEREE")
public class Referee extends User {
    
    public Referee() {
        super();
    }
    
    public Referee(String username, String password, String email) {
        super(username, password, email, UserRole.REFEREE);
    }
}
