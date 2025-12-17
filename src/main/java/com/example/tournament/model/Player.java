package com.example.tournament.model;

import jakarta.persistence.*;

/**
 * Entity representing a player in a team.
 */
@Entity
@Table(name = "players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private int jerseyNumber;
    
    private String position;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    // Constructors
    public Player() {
    }
    
    public Player(String name) {
        this.name = name;
    }
    
    public Player(String name, int jerseyNumber, String position) {
        this.name = name;
        this.jerseyNumber = jerseyNumber;
        this.position = position;
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
    
    public int getJerseyNumber() {
        return jerseyNumber;
    }
    
    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", position='" + position + '\'' +
                '}';
    }
}
