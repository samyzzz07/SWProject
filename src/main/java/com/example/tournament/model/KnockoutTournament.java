package com.example.tournament.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.*;

/**
 * Entity representing a knockout tournament.
 */
@Entity
@Table(name = "knockout_tournaments")
public class KnockoutTournament extends Tournament {
    
    private Integer currentRound;
    
    // Constructors
    public KnockoutTournament() {
        super();
        this.currentRound = 1;
    }
    
    public KnockoutTournament(String name, Sport sport, LocalDate startDate, LocalDate endDate) {
        super(name, sport, startDate, endDate);
        this.currentRound = 1;
    }
    
    // Getters and Setters
    public Integer getCurrentRound() {
        return currentRound;
    }
    
    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }
    
    @Override
    public void generateSchedule() {
        System.out.println("=== Generating Knockout Tournament Schedule ===");
        System.out.println("Tournament: " + getName());
        System.out.println("Number of teams: " + getTeams().size());
        
        List<Team> teams = new ArrayList<>(getTeams());
        
        // Check if number of teams is a power of 2
        int teamCount = teams.size();
        if (teamCount == 0 || (teamCount & (teamCount - 1)) != 0) {
            System.out.println("Warning: Team count is not a power of 2. Some teams will get byes.");
        }
        
        // Shuffle teams for random pairing
        Collections.shuffle(teams);
        
        // Generate first round matches
        System.out.println("\n--- Round 1 Matches ---");
        for (int i = 0; i < teams.size() - 1; i += 2) {
            Team team1 = teams.get(i);
            Team team2 = teams.get(i + 1);
            Match match = new Match(this, team1, team2);
            addMatch(match);
            System.out.println("Match " + (i/2 + 1) + ": " + team1.getName() + " vs " + team2.getName());
        }
        
        System.out.println("\nKnockout schedule generated with " + getMatches().size() + " matches in round 1.");
        System.out.println("Subsequent rounds will be generated as matches are completed.");
    }
    
    @Override
    public Map<Team, Integer> getStandings() {
        System.out.println("=== Knockout Tournament Standings ===");
        System.out.println("Tournament: " + getName());
        
        Map<Team, Integer> standings = new LinkedHashMap<>();
        
        // In knockout tournaments, standings are based on round reached
        // Teams still in competition get higher scores
        for (Team team : getTeams()) {
            // Placeholder logic: all teams start with round 1 standing
            standings.put(team, currentRound);
            System.out.println(team.getName() + " - Round " + currentRound);
        }
        
        return standings;
    }
    
    /**
     * Advances to the next round with winners from current round.
     */
    public void advanceToNextRound(List<Team> winners) {
        System.out.println("Advancing to round " + (currentRound + 1));
        currentRound++;
        
        // Generate matches for next round
        for (int i = 0; i < winners.size() - 1; i += 2) {
            Team team1 = winners.get(i);
            Team team2 = winners.get(i + 1);
            Match match = new Match(this, team1, team2);
            addMatch(match);
            System.out.println("Round " + currentRound + " Match: " + team1.getName() + " vs " + team2.getName());
        }
    }
    
    @Override
    public String toString() {
        return "KnockoutTournament{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", currentRound=" + currentRound +
                ", teamsCount=" + getTeams().size() +
                ", matchesCount=" + getMatches().size() +
                '}';
    }
}
