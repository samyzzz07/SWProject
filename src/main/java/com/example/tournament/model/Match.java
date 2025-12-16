package com.example.tournament.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a match between two teams.
 */
@Entity
@Table(name = "matches")
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    
    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;
    
    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;
    
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
    
    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlot timeSlot;
    
    private LocalDateTime scheduledTime;
    
    private Integer team1Score;
    
    private Integer team2Score;
    
    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    
    // Constructors
    public Match() {
    }
    
    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.SCHEDULED;
    }
    
    public Match(Tournament tournament, Team team1, Team team2) {
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.SCHEDULED;
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
    
    public Team getTeam1() {
        return team1;
    }
    
    public void setTeam1(Team team1) {
        this.team1 = team1;
    }
    
    public Team getTeam2() {
        return team2;
    }
    
    public void setTeam2(Team team2) {
        this.team2 = team2;
    }
    
    public Venue getVenue() {
        return venue;
    }
    
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
    
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
    
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    public Integer getTeam1Score() {
        return team1Score;
    }
    
    public void setTeam1Score(Integer team1Score) {
        this.team1Score = team1Score;
    }
    
    public Integer getTeam2Score() {
        return team2Score;
    }
    
    public void setTeam2Score(Integer team2Score) {
        this.team2Score = team2Score;
    }
    
    public MatchStatus getStatus() {
        return status;
    }
    
    public void setStatus(MatchStatus status) {
        this.status = status;
    }
    
    /**
     * Determines the winner of the match.
     */
    public Team getWinner() {
        if (team1Score == null || team2Score == null) {
            return null;
        }
        if (team1Score > team2Score) {
            return team1;
        } else if (team2Score > team1Score) {
            return team2;
        }
        return null; // Draw
    }
    
    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", team1=" + (team1 != null ? team1.getName() : "null") +
                ", team2=" + (team2 != null ? team2.getName() : "null") +
                ", venue=" + (venue != null ? venue.getName() : "null") +
                ", scheduledTime=" + scheduledTime +
                ", status=" + status +
                '}';
    }
    
    /**
     * Enum for match status.
     */
    public enum MatchStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        POSTPONED
    }
}
