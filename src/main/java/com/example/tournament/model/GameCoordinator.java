package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a GameCoordinator in the tournament system.
 * Responsible for game creation, scheduling, referee management, and tournament operations.
 */
@Entity
@DiscriminatorValue("GAME_COORDINATOR")
public class GameCoordinator extends User {
    
    public GameCoordinator() {
        super();
        setRole(UserRole.GAME_COORDINATOR);
    }
    
    public GameCoordinator(String username, String password, String email) {
        super(username, password, email, UserRole.GAME_COORDINATOR);
    }
}
