package com.example.tournament.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a team manager user.
 */
@Entity
@DiscriminatorValue("TEAM_MANAGER")
public class TeamManager extends User {
    
    @OneToMany(mappedBy = "manager")
    private List<Team> teams = new ArrayList<>();
    
    private String phoneNumber;
    
    // Constructors
    public TeamManager() {
        super();
        setRole(UserRole.TEAM_MANAGER);
    }
    
    public TeamManager(String username, String password, String email) {
        super(username, password, email, UserRole.TEAM_MANAGER);
    }
    
    public TeamManager(String username, String password, String email, String phoneNumber) {
        super(username, password, email, UserRole.TEAM_MANAGER);
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public List<Team> getTeams() {
        return teams;
    }
    
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Placeholder method for registering a team.
     */
    public void registerTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
            team.setManager(this);
            System.out.println("TeamManager " + getUsername() + " registered team: " + team.getName());
        }
    }
    
    /**
     * Placeholder method for managing team time slot preferences.
     */
    public void manageTeamTimeSlots(Team team, TimeSlot timeSlot) {
        team.addPreferredTimeSlot(timeSlot);
        System.out.println("TeamManager " + getUsername() + " added time slot preference for team: " + team.getName());
    }
    
    @Override
    public String toString() {
        return "TeamManager{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", teamsCount=" + teams.size() +
                '}';
    }
}
