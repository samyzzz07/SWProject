package com.example.tournament.model;

import jakarta.persistence.*;

/**
 * Entity representing statistics for a team in a tournament.
 */
@Entity
@Table(name = "team_stats")
public class TeamStats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
    
    // Constructors
    public TeamStats() {
    }
    
    public TeamStats(int matchesPlayed, int wins, int losses, int draws) {
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public Tournament getTournament() {
        return tournament;
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    
    public int getMatchesPlayed() {
        return matchesPlayed;
    }
    
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }
    
    public int getGamesPlayed() {
        return matchesPlayed;
    }
    
    public void setGamesPlayed(int gamesPlayed) {
        this.matchesPlayed = gamesPlayed;
    }
    
    public int getWins() {
        return wins;
    }
    
    public void setWins(int wins) {
        this.wins = wins;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public void setLosses(int losses) {
        this.losses = losses;
    }
    
    public int getDraws() {
        return draws;
    }
    
    public void setDraws(int draws) {
        this.draws = draws;
    }
    
    public int getGoalsFor() {
        return goalsFor;
    }
    
    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }
    
    public int getGoalsAgainst() {
        return goalsAgainst;
    }
    
    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }
    
    public int getGoalsScored() {
        return goalsFor;
    }
    
    public void setGoalsScored(int goalsScored) {
        this.goalsFor = goalsScored;
    }
    
    public int getGoalsConceded() {
        return goalsAgainst;
    }
    
    public void setGoalsConceded(int goalsConceded) {
        this.goalsAgainst = goalsConceded;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }
    
    @Override
    public String toString() {
        return "TeamStats{" +
                "gamesPlayed=" + matchesPlayed +
                ", wins=" + wins +
                ", losses=" + losses +
                ", draws=" + draws +
                ", goalsFor=" + goalsFor +
                ", goalsAgainst=" + goalsAgainst +
                ", points=" + points +
                ", goalDifference=" + getGoalDifference() +
                '}';
    }
}
