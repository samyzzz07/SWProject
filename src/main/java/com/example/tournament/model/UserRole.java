package com.example.tournament.model;

/**
 * Enum representing the role of a user in the system.
 * Based on Sports Tournament Management use case actors.
 */
public enum UserRole {
    ADMINISTRATOR,           // Top-level operations, high-level management
    TEAM,                   // Team-specific tasks, registration
    GAME_COORDINATOR,       // Game creation, scheduling, referee management
    TOURNAMENT_ORGANIZER    // Submitting final scores and results
}
