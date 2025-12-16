package com.example.tournament.model;

import javax.persistence.*;
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
    
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager=" + (manager != null ? manager.getUsername() : "null") +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
