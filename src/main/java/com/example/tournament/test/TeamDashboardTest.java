package com.example.tournament.test;

import com.example.tournament.model.Team;
import com.example.tournament.model.Player;
import com.example.tournament.model.TeamStats;
import com.example.tournament.model.TeamManager;

/**
 * Simple test to verify Team Dashboard functionality.
 * This test verifies the methods that should be available through the ParticipantTeamController.
 */
public class TeamDashboardTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Team Dashboard Functionality Test ===\n");
            
            // Test all required methods
            testAddPlayer();
            testRemovePlayer();
            testGetPlayers();
            testGetTeamStats();
            testHasPlayer();
            testValidateInfo();
            testSaveTeam();
            testRequestApproval();
            testUpdateTeam();
            
            System.out.println("\n=== All Team Dashboard Tests Passed ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void testAddPlayer() {
        System.out.println("Test 1: addPlayer()");
        Team team = new Team("Test Team");
        Player player1 = new Player("John Doe", 10, "Forward");
        Player player2 = new Player("Jane Smith", 5, "Defender");
        
        boolean added1 = team.addPlayer(player1);
        boolean added2 = team.addPlayer(player2);
        
        assert added1 : "Failed to add player 1";
        assert added2 : "Failed to add player 2";
        assert team.getPlayers().size() == 2 : "Expected 2 players, got " + team.getPlayers().size();
        
        System.out.println("  ✓ Successfully added 2 players to team");
    }
    
    private static void testRemovePlayer() {
        System.out.println("Test 2: removePlayer()");
        Team team = new Team("Test Team");
        Player player1 = new Player("John Doe", 10, "Forward");
        player1.setId(1L); // Simulate persisted player
        team.addPlayer(player1);
        
        boolean removed = team.removePlayer(1);
        assert removed : "Failed to remove player";
        assert team.getPlayers().size() == 0 : "Expected 0 players after removal";
        
        System.out.println("  ✓ Successfully removed player from team");
    }
    
    private static void testGetPlayers() {
        System.out.println("Test 3: getPlayers()");
        Team team = new Team("Test Team");
        Player player1 = new Player("John Doe", 10, "Forward");
        Player player2 = new Player("Jane Smith", 5, "Defender");
        
        team.addPlayer(player1);
        team.addPlayer(player2);
        
        assert team.getPlayers() != null : "getPlayers() returned null";
        assert team.getPlayers().size() == 2 : "Expected 2 players";
        
        System.out.println("  ✓ getPlayers() returned correct list of " + team.getPlayers().size() + " players");
    }
    
    private static void testGetTeamStats() {
        System.out.println("Test 4: getTeamStats()");
        Team team = new Team("Test Team");
        
        TeamStats stats = team.getTeamStats();
        assert stats != null : "getTeamStats() returned null";
        
        System.out.println("  ✓ getTeamStats() returned team statistics");
        System.out.println("    Games Played: " + stats.getGamesPlayed());
        System.out.println("    Wins: " + stats.getWins());
        System.out.println("    Losses: " + stats.getLosses());
    }
    
    private static void testHasPlayer() {
        System.out.println("Test 5: hasPlayer()");
        Team team = new Team("Test Team");
        Player player1 = new Player("John Doe", 10, "Forward");
        player1.setId(1L);
        team.addPlayer(player1);
        
        assert team.hasPlayer(1) : "hasPlayer() should return true for existing player";
        assert !team.hasPlayer(999) : "hasPlayer() should return false for non-existing player";
        
        System.out.println("  ✓ hasPlayer() correctly identifies player presence");
    }
    
    private static void testValidateInfo() {
        System.out.println("Test 6: ValidateInfo()");
        Team validTeam = new Team("Valid Team");
        Team invalidTeam = new Team("");
        Team nullTeam = new Team(null);
        
        assert validTeam.ValidateInfo() : "ValidateInfo() should return true for valid team";
        assert !invalidTeam.ValidateInfo() : "ValidateInfo() should return false for empty name";
        assert !nullTeam.ValidateInfo() : "ValidateInfo() should return false for null name";
        
        System.out.println("  ✓ ValidateInfo() correctly validates team information");
    }
    
    private static void testSaveTeam() {
        System.out.println("Test 7: SaveTeam()");
        Team team = new Team("Test Team");
        
        // This is a placeholder method, just verify it doesn't throw exception
        team.SaveTeam();
        
        System.out.println("  ✓ SaveTeam() executed without errors");
    }
    
    private static void testRequestApproval() {
        System.out.println("Test 8: RequestApproval()");
        Team team = new Team("Test Team");
        
        // This is a placeholder method, just verify it doesn't throw exception
        team.RequestApproval();
        
        System.out.println("  ✓ RequestApproval() executed without errors");
    }
    
    private static void testUpdateTeam() {
        System.out.println("Test 9: UpdateTeam()");
        Team team = new Team("Test Team");
        
        // This is a placeholder method, just verify it doesn't throw exception
        team.UpdateTeam();
        
        System.out.println("  ✓ UpdateTeam() executed without errors");
    }
}
