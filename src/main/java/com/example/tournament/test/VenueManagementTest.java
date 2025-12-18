package com.example.tournament.test;

import com.example.tournament.model.Venue;
import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.model.RoundRobinTournament;
import com.example.tournament.model.Sport;
import com.example.tournament.service.VenueService;
import com.example.tournament.service.MatchService;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Simple test to verify venue and match services work correctly.
 * This test does not require JavaFX.
 */
public class VenueManagementTest {
    
    public static void main(String[] args) {
        // Prevent JavaFX initialization
        System.setProperty("javafx.quantum.debug", "false");
        System.setProperty("prism.verbose", "false");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        
        try {
            // Initialize JPA
            System.out.println("Initializing JPA...");
            JPAUtil.initialize();
            
            // Test VenueService
            System.out.println("\n=== Testing VenueService ===");
            testVenueService();
            
            // Test MatchService
            System.out.println("\n=== Testing MatchService ===");
            testMatchService();
            
            // Test TournamentService integration
            System.out.println("\n=== Testing TournamentService Integration ===");
            testTournamentIntegration();
            
            System.out.println("\n=== All Tests Completed Successfully ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown JPA
            JPAUtil.shutdown();
        }
    }
    
    private static void testVenueService() {
        VenueService venueService = new VenueService();
        
        // Create a venue
        Venue venue = new Venue("Test Stadium", "123 Main St", 5000);
        boolean created = venueService.createVenue(venue);
        System.out.println("Venue created: " + created);
        
        // Retrieve all venues
        List<Venue> venues = venueService.getAllVenues();
        System.out.println("Total venues in database: " + venues.size());
        
        // List all venues
        for (Venue v : venues) {
            System.out.println("  - " + v.getName() + " (" + v.getLocation() + ") - Capacity: " + v.getCapacity());
        }
        
        // Update a venue
        if (!venues.isEmpty()) {
            Venue firstVenue = venues.get(0);
            firstVenue.setCapacity(6000);
            boolean updated = venueService.updateVenue(firstVenue);
            System.out.println("Venue updated: " + updated);
        }
    }
    
    private static void testMatchService() {
        MatchService matchService = new MatchService();
        
        // Retrieve all matches
        List<Match> matches = matchService.getAllMatches();
        System.out.println("Total matches in database: " + matches.size());
        
        // List all matches
        for (Match match : matches) {
            System.out.println("  - " + match);
        }
    }
    
    private static void testTournamentIntegration() {
        TournamentService tournamentService = new TournamentService();
        MatchService matchService = new MatchService();
        
        // Create a test sport
        Sport testSport = new Sport("Test Sport");
        
        // Create a test tournament
        Tournament tournament = new RoundRobinTournament(
            "Test Tournament",
            testSport,
            LocalDate.now(),
            LocalDate.now().plusDays(7)
        );
        
        boolean created = tournamentService.createTournament(tournament);
        System.out.println("Tournament created: " + created);
        
        // Retrieve all tournaments
        List<Tournament> tournaments = tournamentService.viewAllTournaments();
        System.out.println("Total tournaments in database: " + tournaments.size());
        
        // List all tournaments
        for (Tournament t : tournaments) {
            System.out.println("  - " + t.getName() + " (" + t.getClass().getSimpleName() + ")");
            
            // Get matches for this tournament
            if (t.getId() != null) {
                List<Match> matches = matchService.getMatchesByTournament(t.getId());
                System.out.println("    Matches: " + matches.size());
            }
        }
    }
}
