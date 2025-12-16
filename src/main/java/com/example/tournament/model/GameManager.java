package com.example.tournament.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a GameManager in the tournament system.
 * The primary administrator responsible for organizing and managing the tournament.
 */
@Entity
@DiscriminatorValue("GAME_MANAGER")
public class GameManager extends User {
    
    public GameManager() {
        super();
    }
    
    public GameManager(String username, String password, String email) {
        super(username, password, email, UserRole.GAME_MANAGER);
    }
    
    public GameManager(String username, String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, UserRole.GAME_MANAGER);
    }
}
