package com.example.tournament.util;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for initializing standard dummy data across the application.
 * This ensures consistency in sample teams, tournaments, and other entities.
 */
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static boolean initialized = false;
    
    /**
     * Initialize standard dummy data if not already done.
     */
    public static synchronized void initializeData() {
        if (initialized) {
            logger.info("Data already initialized, skipping...");
            return;
        }
        
        logger.info("Initializing standard dummy data...");
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Check if data already exists
            Long count = em.createQuery("SELECT COUNT(t) FROM Tournament t", Long.class)
                          .getSingleResult();
            
            if (count > 0) {
                logger.info("Database already contains data, skipping initialization");
                em.getTransaction().rollback();
                initialized = true;
                return;
            }
            
            // Create sports
            Sport football = new Sport("Football");
            Sport basketball = new Sport("Basketball");
            Sport cricket = new Sport("Cricket");
            
            em.persist(football);
            em.persist(basketball);
            em.persist(cricket);
            
            // Create standard tournaments with standardized names
            KnockoutTournament winterChampionship = new KnockoutTournament(
                "Winter Championship 2025",
                basketball,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 31)
            );
            
            RoundRobinTournament springLeague = new RoundRobinTournament(
                "Spring League 2025",
                football,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 31)
            );
            
            RoundRobinTournament summerCup = new RoundRobinTournament(
                "Summer Cup 2025",
                cricket,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 30)
            );
            
            em.persist(winterChampionship);
            em.persist(springLeague);
            em.persist(summerCup);
            
            // Create standard teams with players for each tournament
            // Winter Championship 2025 (Basketball) teams
            Team basketballAlpha = createTeamWithPlayers(em, "Team Alpha", "alpha@team.com");
            Team basketballBeta = createTeamWithPlayers(em, "Team Beta", "beta@team.com");
            Team basketballGamma = createTeamWithPlayers(em, "Team Gamma", "gamma@team.com");
            Team basketballDelta = createTeamWithPlayers(em, "Team Delta", "delta@team.com");
            
            // Spring League 2025 (Football) teams
            Team footballAlpha = createTeamWithPlayers(em, "Team Epsilon", "epsilon@team.com");
            Team footballBeta = createTeamWithPlayers(em, "Team Zeta", "zeta@team.com");
            Team footballGamma = createTeamWithPlayers(em, "Team Eta", "eta@team.com");
            Team footballDelta = createTeamWithPlayers(em, "Team Theta", "theta@team.com");
            
            // Summer Cup 2025 (Cricket) teams
            Team cricketAlpha = createTeamWithPlayers(em, "Team Iota", "iota@team.com");
            Team cricketBeta = createTeamWithPlayers(em, "Team Kappa", "kappa@team.com");
            Team cricketGamma = createTeamWithPlayers(em, "Team Lambda", "lambda@team.com");
            Team cricketDelta = createTeamWithPlayers(em, "Team Mu", "mu@team.com");
            
            // Set approval statuses
            basketballAlpha.setApprovalStatus(Team.STATUS_APPROVED);
            basketballBeta.setApprovalStatus(Team.STATUS_APPROVED);
            basketballGamma.setApprovalStatus(Team.STATUS_APPROVED);
            basketballDelta.setApprovalStatus(Team.STATUS_APPROVED);
            
            footballAlpha.setApprovalStatus(Team.STATUS_APPROVED);
            footballBeta.setApprovalStatus(Team.STATUS_APPROVED);
            footballGamma.setApprovalStatus(Team.STATUS_APPROVED);
            footballDelta.setApprovalStatus(Team.STATUS_APPROVED);
            
            cricketAlpha.setApprovalStatus(Team.STATUS_APPROVED);
            cricketBeta.setApprovalStatus(Team.STATUS_APPROVED);
            cricketGamma.setApprovalStatus(Team.STATUS_APPROVED);
            cricketDelta.setApprovalStatus(Team.STATUS_APPROVED);
            
            // Associate teams with tournaments
            winterChampionship.addTeam(basketballAlpha);
            winterChampionship.addTeam(basketballBeta);
            winterChampionship.addTeam(basketballGamma);
            winterChampionship.addTeam(basketballDelta);
            
            springLeague.addTeam(footballAlpha);
            springLeague.addTeam(footballBeta);
            springLeague.addTeam(footballGamma);
            springLeague.addTeam(footballDelta);
            
            summerCup.addTeam(cricketAlpha);
            summerCup.addTeam(cricketBeta);
            summerCup.addTeam(cricketGamma);
            summerCup.addTeam(cricketDelta);
            
            // Create some venues
            Venue stadiumA = new Venue("Stadium A", "123 Main St", 5000);
            Venue stadiumB = new Venue("Stadium B", "456 Oak Ave", 3000);
            Venue stadiumC = new Venue("Stadium C", "789 Pine Rd", 4000);
            
            em.persist(stadiumA);
            em.persist(stadiumB);
            em.persist(stadiumC);
            
            // Generate schedules for all tournaments to create matches
            winterChampionship.generateSchedule();
            springLeague.generateSchedule();
            summerCup.generateSchedule();
            
            em.getTransaction().commit();
            
            logger.info("Standard dummy data initialized successfully!");
            initialized = true;
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to initialize data: {}", e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Creates a team with sample players.
     */
    private static Team createTeamWithPlayers(EntityManager em, String teamName, String contact) {
        Team team = new Team(teamName);
        team.setContactInfo(contact);
        em.persist(team);
        
        // Add some players
        for (int i = 1; i <= 3; i++) {
            Player player = new Player(
                teamName + " Player " + i,
                i,
                "Position " + i
            );
            player.setTeam(team);
            team.getPlayers().add(player);
            em.persist(player);
        }
        
        return team;
    }
    
    /**
     * Gets list of standard tournament names for UI components.
     */
    public static List<String> getStandardTournamentNames() {
        List<String> names = new ArrayList<>();
        names.add("Winter Championship 2025");
        names.add("Spring League 2025");
        names.add("Summer Cup 2025");
        return names;
    }
    
    /**
     * Gets list of standard team names for UI components.
     */
    public static List<String> getStandardTeamNames() {
        List<String> names = new ArrayList<>();
        names.add("Team Alpha");
        names.add("Team Beta");
        names.add("Team Gamma");
        names.add("Team Delta");
        names.add("Team Epsilon");
        names.add("Team Zeta");
        names.add("Team Eta");
        names.add("Team Theta");
        names.add("Team Iota");
        names.add("Team Kappa");
        names.add("Team Lambda");
        names.add("Team Mu");
        return names;
    }
}
