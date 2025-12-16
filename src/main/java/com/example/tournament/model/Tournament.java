package com.example.tournament.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract entity representing a tournament.
 * Uses JOINED inheritance strategy.
 */
@Entity
@Table(name = "tournaments")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Tournament {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @ManyToMany
    @JoinTable(
        name = "tournament_teams",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>();
    
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private TournamentStatus status;
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private TournamentOrganizer organizer;
    
    // Constructors
    public Tournament() {
    }
    
    public Tournament(String name, Sport sport, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.sport = sport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = TournamentStatus.SCHEDULED;
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
    
    public Sport getSport() {
        return sport;
    }
    
    public void setSport(Sport sport) {
        this.sport = sport;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public List<Team> getTeams() {
        return teams;
    }
    
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    
    public List<Match> getMatches() {
        return matches;
    }
    
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
    
    public TournamentStatus getStatus() {
        return status;
    }
    
    public void setStatus(TournamentStatus status) {
        this.status = status;
    }
    
    public TournamentOrganizer getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(TournamentOrganizer organizer) {
        this.organizer = organizer;
    }
    
    /**
     * Adds a team to the tournament.
     */
    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }
    
    /**
     * Removes a team from the tournament.
     */
    public void removeTeam(Team team) {
        teams.remove(team);
    }
    
    /**
     * Adds a match to the tournament.
     */
    public void addMatch(Match match) {
        if (!matches.contains(match)) {
            matches.add(match);
            match.setTournament(this);
        }
    }
    
    /**
     * Abstract method to generate the tournament schedule.
     * Must be implemented by subclasses.
     */
    public abstract void generateSchedule();
    
    /**
     * Abstract method to get current standings.
     * Must be implemented by subclasses.
     * @return Map of teams to their points/rankings
     */
    public abstract Map<Team, Integer> getStandings();
    
    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sport=" + (sport != null ? sport.getName() : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", teamsCount=" + teams.size() +
                ", matchesCount=" + matches.size() +
                '}';
    }
}
