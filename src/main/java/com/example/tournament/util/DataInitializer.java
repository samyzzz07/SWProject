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
            
            // Create standard tournaments
            RoundRobinTournament summerFootball = new RoundRobinTournament(
                "Summer Football Championship",
                football,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 30)
            );
            
            KnockoutTournament winterBasketball = new KnockoutTournament(
                "Winter Basketball League",
                basketball,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 31)
            );
            
            RoundRobinTournament springCricket = new RoundRobinTournament(
                "Spring Cricket Cup",
                cricket,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 31)
            );
            
            em.persist(summerFootball);
            em.persist(winterBasketball);
            em.persist(springCricket);
            
            // Create standard teams with players
            Team teamAlpha = createTeamWithPlayers(em, "Team Alpha", "alpha@team.com");
            Team teamBeta = createTeamWithPlayers(em, "Team Beta", "beta@team.com");
            Team teamGamma = createTeamWithPlayers(em, "Team Gamma", "gamma@team.com");
            Team teamDelta = createTeamWithPlayers(em, "Team Delta", "delta@team.com");
            Team teamEpsilon = createTeamWithPlayers(em, "Team Epsilon", "epsilon@team.com");
            Team teamZeta = createTeamWithPlayers(em, "Team Zeta", "zeta@team.com");
            
            // Set approval statuses
            teamAlpha.setApprovalStatus("APPROVED");
            teamBeta.setApprovalStatus("PENDING");
            teamGamma.setApprovalStatus("APPROVED");
            teamDelta.setApprovalStatus("PENDING");
            teamEpsilon.setApprovalStatus("APPROVED");
            teamZeta.setApprovalStatus("REJECTED");
            
            // Associate teams with tournaments
            summerFootball.addTeam(teamAlpha);
            summerFootball.addTeam(teamBeta);
            summerFootball.addTeam(teamGamma);
            
            winterBasketball.addTeam(teamDelta);
            winterBasketball.addTeam(teamEpsilon);
            
            springCricket.addTeam(teamZeta);
            
            // Create some venues
            Venue stadiumA = new Venue("Stadium A", "123 Main St", 5000);
            Venue stadiumB = new Venue("Stadium B", "456 Oak Ave", 3000);
            Venue stadiumC = new Venue("Stadium C", "789 Pine Rd", 4000);
            
            em.persist(stadiumA);
            em.persist(stadiumB);
            em.persist(stadiumC);
            
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
        names.add("Summer Football Championship");
        names.add("Winter Basketball League");
        names.add("Spring Cricket Cup");
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
        return names;
    }
}
