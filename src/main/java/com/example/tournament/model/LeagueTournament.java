package com.example.tournament.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.*;

/**
 * Entity representing a league tournament (round-robin format).
 */
@Entity
@Table(name = "league_tournaments")
public class LeagueTournament extends Tournament {
    
    private Integer pointsForWin = 3;
    private Integer pointsForDraw = 1;
    private Integer pointsForLoss = 0;
    
    // Constructors
    public LeagueTournament() {
        super();
    }
    
    public LeagueTournament(String name, Sport sport, LocalDate startDate, LocalDate endDate) {
        super(name, sport, startDate, endDate);
    }
    
    // Getters and Setters
    public Integer getPointsForWin() {
        return pointsForWin;
    }
    
    public void setPointsForWin(Integer pointsForWin) {
        this.pointsForWin = pointsForWin;
    }
    
    public Integer getPointsForDraw() {
        return pointsForDraw;
    }
    
    public void setPointsForDraw(Integer pointsForDraw) {
        this.pointsForDraw = pointsForDraw;
    }
    
    public Integer getPointsForLoss() {
        return pointsForLoss;
    }
    
    public void setPointsForLoss(Integer pointsForLoss) {
        this.pointsForLoss = pointsForLoss;
    }
    
    @Override
    public void generateSchedule() {
        System.out.println("=== Generating League Tournament Schedule (Round-Robin) ===");
        System.out.println("Tournament: " + getName());
        System.out.println("Number of teams: " + getTeams().size());
        
        List<Team> teams = new ArrayList<>(getTeams());
        
        if (teams.size() < 2) {
            System.out.println("Error: Need at least 2 teams for a league tournament.");
            return;
        }
        
        int matchNumber = 1;
        
        // Generate round-robin schedule: each team plays every other team once
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team team1 = teams.get(i);
                Team team2 = teams.get(j);
                Match match = new Match(this, team1, team2);
                addMatch(match);
                System.out.println("Match " + matchNumber + ": " + team1.getName() + " vs " + team2.getName());
                matchNumber++;
            }
        }
        
        System.out.println("\nLeague schedule generated with " + getMatches().size() + " total matches.");
        System.out.println("Each team will play " + (teams.size() - 1) + " matches.");
    }
    
    @Override
    public Map<Team, Integer> getStandings() {
        System.out.println("=== League Tournament Standings ===");
        System.out.println("Tournament: " + getName());
        
        Map<Team, Integer> standings = new HashMap<>();
        
        // Initialize all teams with 0 points
        for (Team team : getTeams()) {
            standings.put(team, 0);
        }
        
        // Calculate points based on match results
        for (Match match : getMatches()) {
            if (match.getStatus() == Match.MatchStatus.COMPLETED && 
                match.getTeam1Score() != null && match.getTeam2Score() != null) {
                
                Team team1 = match.getTeam1();
                Team team2 = match.getTeam2();
                
                if (match.getTeam1Score() > match.getTeam2Score()) {
                    // Team 1 wins
                    standings.put(team1, standings.get(team1) + pointsForWin);
                    standings.put(team2, standings.get(team2) + pointsForLoss);
                } else if (match.getTeam2Score() > match.getTeam1Score()) {
                    // Team 2 wins
                    standings.put(team2, standings.get(team2) + pointsForWin);
                    standings.put(team1, standings.get(team1) + pointsForLoss);
                } else {
                    // Draw
                    standings.put(team1, standings.get(team1) + pointsForDraw);
                    standings.put(team2, standings.get(team2) + pointsForDraw);
                }
            }
        }
        
        // Sort standings by points (descending)
        List<Map.Entry<Team, Integer>> sortedStandings = new ArrayList<>(standings.entrySet());
        sortedStandings.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Create ordered map
        Map<Team, Integer> orderedStandings = new LinkedHashMap<>();
        int rank = 1;
        for (Map.Entry<Team, Integer> entry : sortedStandings) {
            orderedStandings.put(entry.getKey(), entry.getValue());
            System.out.println(rank + ". " + entry.getKey().getName() + " - " + entry.getValue() + " points");
            rank++;
        }
        
        return orderedStandings;
    }
    
    @Override
    public String toString() {
        return "LeagueTournament{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", pointsForWin=" + pointsForWin +
                ", teamsCount=" + getTeams().size() +
                ", matchesCount=" + getMatches().size() +
                '}';
    }
}
