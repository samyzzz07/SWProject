package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a Player in the tournament system.
 * A participant in the tournament.
 */
@Entity
@DiscriminatorValue("PLAYER")
public class Player extends User {
    
    public Player() {
        super();
    }
    
    public Player(String username, String password, String email) {
        super(username, password, email, UserRole.PLAYER);
    }
}
