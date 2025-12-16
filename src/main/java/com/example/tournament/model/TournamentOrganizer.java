package com.example.tournament.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a tournament organizer user.
 * Responsible for submitting final scores and results.
 */
@Entity
@DiscriminatorValue("TOURNAMENT_ORGANIZER")
public class TournamentOrganizer extends User {
    
    private String organization;
    
    @OneToMany(mappedBy = "organizer")
    private List<Tournament> tournaments = new ArrayList<>();
    
    // Constructors
    public TournamentOrganizer() {
        super();
        setRole(UserRole.TOURNAMENT_ORGANIZER);
    }
    
    public TournamentOrganizer(String username, String password, String email) {
        super(username, password, email, UserRole.TOURNAMENT_ORGANIZER);
    }
    
    public TournamentOrganizer(String username, String password, String email, String organization) {
        super(username, password, email, UserRole.TOURNAMENT_ORGANIZER);
        this.organization = organization;
    }
    
    // Getters and Setters
    public String getOrganization() {
        return organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    public List<Tournament> getTournaments() {
        return tournaments;
    }
    
    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
    
    /**
     * Placeholder method for creating a tournament.
     */
    public void createTournament(Tournament tournament) {
        tournaments.add(tournament);
        tournament.setOrganizer(this);
        System.out.println("TournamentOrganizer " + getUsername() + " created tournament: " + tournament.getName());
    }
    
    /**
     * Placeholder method for managing tournaments.
     */
    public void manageTournaments() {
        System.out.println("TournamentOrganizer " + getUsername() + " is managing " + tournaments.size() + " tournaments.");
    }
    
    @Override
    public String toString() {
        return "TournamentOrganizer{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", organization='" + organization + '\'' +
                ", tournamentsCount=" + tournaments.size() +
                '}';
    }
}
