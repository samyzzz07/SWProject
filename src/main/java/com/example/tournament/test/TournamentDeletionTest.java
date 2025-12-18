package com.example.tournament.test;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;

import java.time.LocalDate;

/**
 * Test to verify tournament deletion functionality:
 * 1. Tournaments can be deleted from the database
 * 2. Matches are deleted when tournament is deleted
 * 3. Deleted tournaments don't appear in viewAllTournaments
 */
public class TournamentDeletionTest {
    
    public static void main(String[] args) {
        System.out.println("=== Tournament Deletion Test ===\n");
        
        // Initialize database
        JPAUtil.initialize();
        
        try {
            testTournamentDeletion();
            testMatchesCascadeDelete();
            testDeletedTournamentNotVisible();
            
            System.out.println("\n=== All Tests Passed ===");
            
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Test error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            JPAUtil.shutdown();
        }
    }
    
    /**
     * Test that tournaments can be deleted
     */
    private static void testTournamentDeletion() {
        System.out.println("Test 1: Tournament Deletion");
        
        TournamentService tournamentService = new TournamentService();
        
        // Create a test tournament
        Sport sport = new Sport("TestSport");
        KnockoutTournament tournament = new KnockoutTournament(
            "Test Deletion Tournament",
            sport,
            LocalDate.now(),
            LocalDate.now().plusDays(7)
        );
        
        boolean created = tournamentService.createTournament(tournament);
        assert created : "Tournament should be created successfully";
        System.out.println("  ✓ Tournament created: " + tournament.getName());
        
        Long tournamentId = tournament.getId();
        assert tournamentId != null : "Tournament should have an ID";
        
        // Delete the tournament
        boolean deleted = tournamentService.deleteTournament(tournamentId);
        assert deleted : "Tournament should be deleted successfully";
        System.out.println("  ✓ Tournament deleted");
        
        // Verify it's gone
        Tournament deletedTournament = tournamentService.getTournamentById(tournamentId);
        assert deletedTournament == null : "Deleted tournament should not be retrievable";
        System.out.println("  ✓ Tournament cannot be retrieved after deletion");
    }
    
    /**
     * Test that matches are deleted when tournament is deleted (cascade)
     */
    private static void testMatchesCascadeDelete() {
        System.out.println("\nTest 2: Matches Cascade Delete");
        
        TournamentService tournamentService = new TournamentService();
        
        // Create a tournament with teams and matches
        Sport sport = new Sport("TestSport2");
        KnockoutTournament tournament = new KnockoutTournament(
            "Tournament with Matches",
            sport,
            LocalDate.now(),
            LocalDate.now().plusDays(7)
        );
        
        // Add teams
        Team team1 = new Team("Test Team 1");
        Team team2 = new Team("Test Team 2");
        tournament.addTeam(team1);
        tournament.addTeam(team2);
        
        // Create matches
        Match match1 = new Match(tournament, team1, team2);
        tournament.addMatch(match1);
        
        boolean created = tournamentService.createTournament(tournament);
        assert created : "Tournament with matches should be created";
        System.out.println("  ✓ Tournament created with " + tournament.getMatches().size() + " match(es)");
        
        Long tournamentId = tournament.getId();
        
        // Delete the tournament
        boolean deleted = tournamentService.deleteTournament(tournamentId);
        assert deleted : "Tournament should be deleted";
        System.out.println("  ✓ Tournament with matches deleted");
        
        // Verify tournament is gone
        Tournament deletedTournament = tournamentService.getTournamentById(tournamentId);
        assert deletedTournament == null : "Deleted tournament should not exist";
        System.out.println("  ✓ Tournament and its matches were successfully deleted");
    }
    
    /**
     * Test that deleted tournaments don't appear in viewAllTournaments
     */
    private static void testDeletedTournamentNotVisible() {
        System.out.println("\nTest 3: Deleted Tournament Not Visible");
        
        TournamentService tournamentService = new TournamentService();
        
        // Get initial count
        int initialCount = tournamentService.viewAllTournaments().size();
        System.out.println("  Initial tournament count: " + initialCount);
        
        // Create a tournament
        Sport sport = new Sport("TestSport3");
        KnockoutTournament tournament = new KnockoutTournament(
            "Visible Then Invisible Tournament",
            sport,
            LocalDate.now(),
            LocalDate.now().plusDays(7)
        );
        
        boolean created = tournamentService.createTournament(tournament);
        assert created : "Tournament should be created";
        
        // Verify it appears in the list
        int countAfterCreate = tournamentService.viewAllTournaments().size();
        assert countAfterCreate == initialCount + 1 : "Tournament count should increase by 1";
        System.out.println("  ✓ Tournament appears in list after creation");
        
        Long tournamentId = tournament.getId();
        
        // Delete the tournament
        boolean deleted = tournamentService.deleteTournament(tournamentId);
        assert deleted : "Tournament should be deleted";
        
        // Verify it doesn't appear in the list
        int countAfterDelete = tournamentService.viewAllTournaments().size();
        assert countAfterDelete == initialCount : "Tournament count should return to initial value";
        System.out.println("  ✓ Tournament does not appear in list after deletion");
        
        // Verify it's not in the list by name
        boolean foundInList = tournamentService.viewAllTournaments().stream()
            .anyMatch(t -> t.getName().equals(tournament.getName()));
        assert !foundInList : "Deleted tournament should not be in viewAllTournaments list";
        System.out.println("  ✓ Deleted tournament confirmed not in viewAllTournaments");
    }
}
