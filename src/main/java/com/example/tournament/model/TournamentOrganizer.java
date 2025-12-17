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
    
    /**
     * Views all registered teams across all tournaments.
     */
    public void viewregisteredteams() {
        System.out.println("TournamentOrganizer " + getUsername() + " is viewing registered teams.");
        for (Tournament tournament : tournaments) {
            System.out.println("Tournament: " + tournament.getName());
            // In a real application, this would fetch and display teams
        }
    }
    
    /**
     * Approves a team for participation in a tournament.
     */
    public void approveTeam() {
        System.out.println("TournamentOrganizer " + getUsername() + " is approving a team.");
        // In a real application, this would update team approval status
    }
    
    /**
     * Publishes the tournament schedule.
     * @return the published schedule
     */
    public Schedule publishSchedule() {
        System.out.println("TournamentOrganizer " + getUsername() + " is publishing a schedule.");
        // In a real application, this would create and publish a schedule
        Schedule schedule = new Schedule();
        schedule.setPublished(true);
        schedule.setPublishedDate(java.time.LocalDateTime.now());
        return schedule;
    }
    
    /**
     * Collects fees from teams.
     */
    public void collectFees() {
        System.out.println("TournamentOrganizer " + getUsername() + " is collecting fees.");
        // In a real application, this would handle fee collection
    }
    
    /**
     * Finds sponsors for the tournament.
     */
    public void findSponsor() {
        System.out.println("TournamentOrganizer " + getUsername() + " is finding sponsors.");
        // In a real application, this would manage sponsor relationships
    }
    
    /**
     * Checks the tournament schedule.
     */
    public void checkSchedule() {
        System.out.println("TournamentOrganizer " + getUsername() + " is checking the schedule.");
        // In a real application, this would validate the schedule
    }
    
    /**
     * Assigns referees to matches.
     */
    public void assignReferees() {
        System.out.println("TournamentOrganizer " + getUsername() + " is assigning referees.");
        // In a real application, this would assign referees to matches
    }
    
    /**
     * Postpones a match in the tournament.
     */
    public void postponeMatch() {
        System.out.println("TournamentOrganizer " + getUsername() + " is postponing a match.");
        // In a real application, this would update match status to postponed
    }
    
    /**
     * Shows a request menu popup.
     */
    public void showRequestMenuPopUp() {
        System.out.println("TournamentOrganizer " + getUsername() + " is showing request menu popup.");
        // In a real application, this would display a UI popup
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
