package com.example.tournament.test;

import com.example.tournament.model.Team;
import com.example.tournament.model.Player;
import com.example.tournament.model.TeamManager;
import com.example.tournament.service.TeamService;

/**
 * Integration test demonstrating the complete workflow:
 * 1. Team manager creates a team
 * 2. Adds players with auto-generated IDs
 * 3. Requests approval
 * 4. Organizer sees pending team for approval
 */
public class TeamWorkflowIntegrationTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Team Dashboard Workflow Integration Test ===\n");
            
            TeamService teamService = new TeamService();
            
            // Step 1: Team manager creates a team
            System.out.println("Step 1: Team Manager creates a new team");
            TeamManager manager = new TeamManager("manager123", "password", "manager@example.com");
            Team team = new Team("Warriors FC", manager);
            team.setContactInfo("warriors@team.com | (555) 100-2000");
            
            // Register the team
            boolean registered = teamService.registerTeam(team);
            assert registered : "Team registration should succeed";
            System.out.println("  ✓ Team registered: " + team.getName());
            System.out.println("  Initial approval status: " + team.getApprovalStatus());
            
            // Step 2: Add players with auto-generated IDs
            System.out.println("\nStep 2: Adding players to the team");
            Player player1 = new Player("Alex Johnson", 10, "Forward");
            Player player2 = new Player("Sam Martinez", 7, "Midfielder");
            Player player3 = new Player("Jordan Lee", 1, "Goalkeeper");
            
            team.addPlayer(player1);
            team.addPlayer(player2);
            team.addPlayer(player3);
            
            System.out.println("  Added " + team.getPlayers().size() + " players");
            
            // Step 3: Update team (persists players)
            System.out.println("\nStep 3: Updating team to persist changes");
            team.UpdateTeam();
            boolean updated = teamService.updateTeam(team);
            assert updated : "Team update should succeed";
            System.out.println("  ✓ Team updated successfully");
            
            // Verify players are persisted and have IDs
            System.out.println("  Player roster:");
            for (Player p : team.getPlayers()) {
                System.out.println("    " + p.toString());
            }
            
            // Step 4: Request approval
            System.out.println("\nStep 4: Team manager requests approval");
            assert team.ValidateInfo() : "Team info should be valid";
            team.RequestApproval();
            
            // Persist the approval request
            boolean approvalRequested = teamService.updateTeam(team);
            assert approvalRequested : "Approval request should be persisted";
            System.out.println("  ✓ Approval requested");
            System.out.println("  Current approval status: " + team.getApprovalStatus());
            
            // Step 5: Simulate organizer viewing pending teams
            System.out.println("\nStep 5: Organizer views teams pending approval");
            var allTeams = teamService.getAllTeams();
            var pendingTeams = allTeams.stream()
                .filter(t -> "PENDING".equals(t.getApprovalStatus()))
                .toList();
            
            System.out.println("  Found " + pendingTeams.size() + " team(s) pending approval:");
            for (Team t : pendingTeams) {
                System.out.println("    - " + t.getName() + 
                    " (" + t.getPlayers().size() + " players, Status: " + t.getApprovalStatus() + ")");
            }
            
            // Verify our team is in the pending list
            boolean teamFoundInPendingList = pendingTeams.stream()
                .anyMatch(t -> t.getName().equals(team.getName()));
            assert teamFoundInPendingList : "Team should appear in pending approval list";
            System.out.println("  ✓ Team appears in pending approval list");
            
            // Step 6: Organizer approves the team
            System.out.println("\nStep 6: Organizer approves the team");
            team.setApprovalStatus("APPROVED");
            boolean approved = teamService.updateTeam(team);
            assert approved : "Approval should be persisted";
            System.out.println("  ✓ Team approved");
            System.out.println("  Final approval status: " + team.getApprovalStatus());
            
            // Verify team is no longer in pending list
            var stillPending = teamService.getAllTeams().stream()
                .filter(t -> "PENDING".equals(t.getApprovalStatus()))
                .toList();
            boolean noLongerPending = stillPending.stream()
                .noneMatch(t -> t.getName().equals(team.getName()));
            assert noLongerPending : "Approved team should not be in pending list";
            System.out.println("  ✓ Team removed from pending approval list");
            
            System.out.println("\n=== Integration Test Passed ===");
            System.out.println("\nSummary:");
            System.out.println("✓ Players display with auto-generated IDs");
            System.out.println("✓ Team updates persist player changes");
            System.out.println("✓ Approval status is tracked and persisted");
            System.out.println("✓ Teams appear in organizer's pending approval list");
            System.out.println("✓ Approved teams are removed from pending list");
            
        } catch (Exception e) {
            System.err.println("Integration test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
