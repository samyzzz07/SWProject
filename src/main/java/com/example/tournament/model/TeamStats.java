package com.example.tournament.model;

/**
 * Class representing statistics for a team.
 */
public class TeamStats {
    
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int goalsScored;
    private int goalsConceded;
    
    // Constructors
    public TeamStats() {
    }
    
    public TeamStats(int gamesPlayed, int wins, int losses, int draws) {
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    
    // Getters and Setters
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
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
    
    public int getGoalsScored() {
        return goalsScored;
    }
    
    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }
    
    public int getGoalsConceded() {
        return goalsConceded;
    }
    
    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }
    
    public int getPoints() {
        return (wins * 3) + draws;
    }
    
    public int getGoalDifference() {
        return goalsScored - goalsConceded;
    }
    
    @Override
    public String toString() {
        return "TeamStats{" +
                "gamesPlayed=" + gamesPlayed +
                ", wins=" + wins +
                ", losses=" + losses +
                ", draws=" + draws +
                ", goalsScored=" + goalsScored +
                ", goalsConceded=" + goalsConceded +
                ", points=" + getPoints() +
                ", goalDifference=" + getGoalDifference() +
                '}';
    }
}
