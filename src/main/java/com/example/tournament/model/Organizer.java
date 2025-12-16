package com.example.tournament.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an organizer user.
 */
@Entity
@DiscriminatorValue("ORGANIZER")
public class Organizer extends User {
    
    private String organization;
    
    @OneToMany(mappedBy = "organizer")
    private List<Tournament> tournaments = new ArrayList<>();
    
    // Constructors
    public Organizer() {
        super();
        setRole(UserRole.ORGANIZER);
    }
    
    public Organizer(String username, String password, String email) {
        super(username, password, email, UserRole.ORGANIZER);
    }
    
    public Organizer(String username, String password, String email, String organization) {
        super(username, password, email, UserRole.ORGANIZER);
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
        System.out.println("Organizer " + getUsername() + " created tournament: " + tournament.getName());
    }
    
    /**
     * Placeholder method for managing tournaments.
     */
    public void manageTournaments() {
        System.out.println("Organizer " + getUsername() + " is managing " + tournaments.size() + " tournaments.");
    }
    
    @Override
    public String toString() {
        return "Organizer{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", organization='" + organization + '\'' +
                ", tournamentsCount=" + tournaments.size() +
                '}';
    }
}
