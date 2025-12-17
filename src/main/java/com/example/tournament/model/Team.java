package com.example.tournament.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a team participating in tournaments.
 */
@Entity
@Table(name = "teams")
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "team_manager_id")
    private TeamManager manager;
    
    @ManyToMany
    @JoinTable(
        name = "team_preferred_timeslots",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "timeslot_id")
    )
    private List<TimeSlot> preferredTimeSlots = new ArrayList<>();
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
    
    private String contactInfo;
    
    // Constructors
    public Team() {
    }
    
    public Team(String name) {
        this.name = name;
    }
    
    public Team(String name, TeamManager manager) {
        this.name = name;
        this.manager = manager;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TeamManager getManager() {
        return manager;
    }
    
    public void setManager(TeamManager manager) {
        this.manager = manager;
    }
    
    public List<TimeSlot> getPreferredTimeSlots() {
        return preferredTimeSlots;
    }
    
    public void setPreferredTimeSlots(List<TimeSlot> preferredTimeSlots) {
        this.preferredTimeSlots = preferredTimeSlots;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public void addPreferredTimeSlot(TimeSlot timeSlot) {
        if (!this.preferredTimeSlots.contains(timeSlot)) {
            this.preferredTimeSlots.add(timeSlot);
        }
    }
    
    public void removePreferredTimeSlot(TimeSlot timeSlot) {
        this.preferredTimeSlots.remove(timeSlot);
    }
    
    /**
     * Adds a player to the team.
     * @param p the player to add
     * @return true if player was added successfully, false otherwise
     */
    public boolean addPlayer(Player p) {
        if (p != null && (p.getId() == null || !hasPlayer((int)(long)p.getId()))) {
            players.add(p);
            p.setTeam(this);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a player from the team by player ID.
     * @param playerId the ID of the player to remove
     * @return true if player was removed successfully, false otherwise
     */
    public boolean removePlayer(int playerId) {
        return players.removeIf(player -> player.getId() != null && player.getId() == (long)playerId);
    }
    
    /**
     * Gets the list of players in the team.
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Sets the list of players for the team.
     * @param players list of players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    
    /**
     * Gets the team statistics.
     * @return team statistics
     */
    public TeamStats getTeamStats() {
        // This is a placeholder implementation
        // In a real application, this would calculate stats from match results
        TeamStats stats = new TeamStats();
        stats.setGamesPlayed(0);
        stats.setWins(0);
        stats.setLosses(0);
        stats.setDraws(0);
        return stats;
    }
    
    /**
     * Checks if the team has a player with the given ID.
     * @param playerId the ID of the player to check
     * @return true if player exists in team, false otherwise
     */
    public boolean hasPlayer(int playerId) {
        return players.stream().anyMatch(p -> p.getId() != null && p.getId() == (long)playerId);
    }
    
    /**
     * Validates the team information.
     * @return true if team info is valid, false otherwise
     */
    public boolean ValidateInfo() {
        return name != null && !name.trim().isEmpty();
    }
    
    /**
     * Saves the team (placeholder method).
     * In a real application, this would persist the team to the database.
     */
    public void SaveTeam() {
        System.out.println("Team " + name + " saved successfully.");
    }
    
    /**
     * Requests approval for the team (placeholder method).
     * In a real application, this would create an approval request.
     */
    public void RequestApproval() {
        System.out.println("Team " + name + " requested approval.");
    }
    
    /**
     * Updates the team information (placeholder method).
     * In a real application, this would update the team in the database.
     */
    public void UpdateTeam() {
        System.out.println("Team " + name + " updated successfully.");
    }
    
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager=" + (manager != null ? manager.getUsername() : "null") +
                ", contactInfo='" + contactInfo + '\'' +
                ", playersCount=" + players.size() +
                '}';
    }
}
