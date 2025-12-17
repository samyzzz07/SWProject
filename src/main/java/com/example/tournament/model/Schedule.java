package com.example.tournament.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a schedule for a tournament.
 */
@Entity
@Table(name = "schedules")
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime publishedDate;
    
    private boolean isPublished;
    
    // Constructors
    public Schedule() {
    }
    
    public Schedule(Tournament tournament) {
        this.tournament = tournament;
        this.isPublished = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Tournament getTournament() {
        return tournament;
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    
    public List<Match> getMatches() {
        return matches;
    }
    
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
    
    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }
    
    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public boolean isPublished() {
        return isPublished;
    }
    
    public void setPublished(boolean published) {
        isPublished = published;
    }
    
    public void addMatch(Match match) {
        if (!matches.contains(match)) {
            matches.add(match);
            match.setSchedule(this);
        }
    }
    
    public void removeMatch(Match match) {
        matches.remove(match);
        match.setSchedule(null);
    }
    
    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", tournament=" + (tournament != null ? tournament.getName() : "null") +
                ", matchesCount=" + matches.size() +
                ", publishedDate=" + publishedDate +
                ", isPublished=" + isPublished +
                '}';
    }
}
