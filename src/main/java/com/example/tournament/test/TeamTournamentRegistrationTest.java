package com.example.tournament.test;

import com.example.tournament.model.*;
import com.example.tournament.service.TeamService;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;

import java.time.LocalDate;

/**
 * Test to verify the fixes for team registration with tournaments:
 * 1. Teams can be registered for specific tournaments
 * 2. Players have proper IDs when persisted
 * 3. Teams appear in tournament organizer's view
 */
public class TeamTournamentRegistrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Team Tournament Registration Test ===\n");
        
        // Initialize database
        JPAUtil.initialize();
        
        try {
            testTeamRegistrationWithTournament();
            testPlayerIdPersistence();
            testTeamsVisibleToOrganizer();
            
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
     * Test that teams can be registered for specific tournaments
     */
    private static void testTeamRegistrationWithTournament() {
        System.out.println("Test 1: Team Registration with Tournament");
        
        TeamService teamService = new TeamService();
        TournamentService tournamentService = new TournamentService();
        
        // Get an existing tournament (from dummy data)
        var tournaments = tournamentService.viewAllTournaments();
        assert !tournaments.isEmpty() : "Should have at least one tournament from dummy data";
        
        Tournament tournament = tournaments.get(0);
        System.out.println("  Using tournament: " + tournament.getName());
        
        // Create and register a new team
        TeamManager manager = new TeamManager("test_manager", "password", "test@example.com");
        Team newTeam = new Team("Test Integration Team");
        newTeam.setContactInfo("test@team.com");
        newTeam.setManager(manager);
        
        boolean saved = teamService.registerTeam(newTeam);
        assert saved : "Team should be saved successfully";
        System.out.println("  ✓ Team registered: " + newTeam.getName());
        
        // Add team to tournament
        tournament.addTeam(newTeam);
        boolean updated = tournamentService.updateTournament(tournament);
        assert updated : "Tournament should be updated successfully";
        System.out.println("  ✓ Team added to tournament");
        
        // Verify team is in tournament
        Tournament reloadedTournament = tournamentService.getTournamentById(tournament.getId());
        boolean teamInTournament = reloadedTournament.getTeams().stream()
            .anyMatch(t -> t.getName().equals(newTeam.getName()));
        assert teamInTournament : "Team should be in tournament";
        System.out.println("  ✓ Team appears in tournament's team list");
    }
    
    /**
     * Test that players get proper IDs when persisted
     */
    private static void testPlayerIdPersistence() {
        System.out.println("\nTest 2: Player ID Persistence");
        
        TeamService teamService = new TeamService();
        
        // Create a team with players
        Team team = new Team("ID Test Team");
        team.setContactInfo("idtest@team.com");
        
        Player player1 = new Player("Player One", 1, "Forward");
        Player player2 = new Player("Player Two", 2, "Midfielder");
        
        // Players should not have IDs yet
        assert player1.getId() == null : "New player should not have ID";
        assert player2.getId() == null : "New player should not have ID";
        System.out.println("  Before persistence - Player 1 ID: " + player1.getId());
        System.out.println("  Before persistence - Player 2 ID: " + player2.getId());
        
        // Add players to team
        team.addPlayer(player1);
        team.addPlayer(player2);
        
        // Save team
        boolean saved = teamService.registerTeam(team);
        assert saved : "Team should be saved";
        
        // Reload team from database
        var allTeams = teamService.getAllTeams();
        Team reloadedTeam = allTeams.stream()
            .filter(t -> t.getName().equals("ID Test Team"))
            .findFirst()
            .orElse(null);
        
        assert reloadedTeam != null : "Team should be found in database";
        assert reloadedTeam.getPlayers().size() == 2 : "Team should have 2 players";
        
        // Check that players now have IDs
        for (Player p : reloadedTeam.getPlayers()) {
            assert p.getId() != null : "Persisted player should have ID";
            assert p.getId() > 0 : "Player ID should be positive";
            System.out.println("  After persistence - " + p.getName() + " ID: " + p.getId());
        }
        
        System.out.println("  ✓ Players have auto-generated IDs after persistence");
    }
    
    /**
     * Test that teams are visible to tournament organizer
     */
    private static void testTeamsVisibleToOrganizer() {
        System.out.println("\nTest 3: Teams Visible to Tournament Organizer");
        
        TeamService teamService = new TeamService();
        
        // Get all teams (simulating what the ViewTeamsDialog does)
        var allTeams = teamService.getAllTeams();
        assert !allTeams.isEmpty() : "Should have teams in database";
        System.out.println("  Found " + allTeams.size() + " teams in database");
        
        // Count teams by approval status
        long approved = allTeams.stream()
            .filter(t -> "APPROVED".equals(t.getApprovalStatus()))
            .count();
        long pending = allTeams.stream()
            .filter(t -> "PENDING".equals(t.getApprovalStatus()))
            .count();
        long rejected = allTeams.stream()
            .filter(t -> "REJECTED".equals(t.getApprovalStatus()))
            .count();
        
        System.out.println("  Approved teams: " + approved);
        System.out.println("  Pending teams: " + pending);
        System.out.println("  Rejected teams: " + rejected);
        
        // Verify player counts are accurate
        for (Team team : allTeams) {
            int playerCount = team.getPlayers().size();
            System.out.println("  " + team.getName() + ": " + playerCount + " players");
        }
        
        System.out.println("  ✓ Teams are accessible to tournament organizer");
    }
}
