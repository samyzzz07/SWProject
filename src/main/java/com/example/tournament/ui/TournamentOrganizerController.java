package com.example.tournament.ui;

import com.example.tournament.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JavaFX Controller for tournament organizer dashboard.
 */
public class TournamentOrganizerController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button viewTeamsButton;
    
    @FXML
    private Button approveTeamButton;
    
    @FXML
    private Button publishScheduleButton;
    
    @FXML
    private Button collectFeesButton;
    
    @FXML
    private Button findSponsorButton;
    
    @FXML
    private Button checkScheduleButton;
    
    @FXML
    private Button assignRefereesButton;
    
    @FXML
    private Button postponeMatchButton;
    
    @FXML
    private Button showRequestMenuButton;
    
    @FXML
    private TextArea activityLogArea;
    
    // Current user
    private User currentUser;
    private TournamentOrganizer currentOrganizer;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("Initializing TournamentOrganizerController...");
        
        statusLabel.setText("Ready");
        activityLogArea.setText("Tournament Organizer Dashboard initialized.\n");
        
        System.out.println("Initialization complete.");
    }
    
    /**
     * Handles the View Registered Teams button click.
     */
    @FXML
    private void handleViewRegisteredTeams() {
        System.out.println("=== View Registered Teams Action ===");
        
        try {
            // Call the organizer's viewregisteredteams method
            if (currentOrganizer != null) {
                currentOrganizer.viewregisteredteams();
            }
            
            // Log the action
            logActivity("Viewed registered teams");
            
            statusLabel.setText("Displaying registered teams");
            System.out.println("✓ Registered teams displayed");
            
            // Show information
            showAlert("Registered Teams", "Viewing all registered teams across tournaments.\n\n" +
                     "Sample Teams:\n" +
                     "- Team Alpha (Status: Approved)\n" +
                     "- Team Beta (Status: Pending)\n" +
                     "- Team Gamma (Status: Approved)\n" +
                     "- Team Delta (Status: Pending)");
            
        } catch (Exception e) {
            statusLabel.setText("Error viewing teams: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to view registered teams: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Approve Team button click.
     */
    @FXML
    private void handleApproveTeam() {
        System.out.println("=== Approve Team Action ===");
        
        try {
            // Call the organizer's approveTeam method
            if (currentOrganizer != null) {
                currentOrganizer.approveTeam();
            }
            
            // Log the action
            logActivity("Approved a team");
            
            statusLabel.setText("Team approved successfully!");
            System.out.println("✓ Team approved");
            
            // Show confirmation
            showAlert("Success", "Team has been approved for tournament participation!");
            
        } catch (Exception e) {
            statusLabel.setText("Error approving team: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to approve team: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Publish Schedule button click.
     */
    @FXML
    private void handlePublishSchedule() {
        System.out.println("=== Publish Schedule Action ===");
        
        try {
            // Call the organizer's publishSchedule method
            Schedule schedule = null;
            if (currentOrganizer != null) {
                schedule = currentOrganizer.publishSchedule();
            }
            
            // Log the action
            logActivity("Published tournament schedule");
            
            statusLabel.setText("Schedule published successfully!");
            System.out.println("✓ Schedule published");
            
            // Show confirmation
            String message = "Tournament schedule has been published!\n\n";
            if (schedule != null && schedule.getPublishedDate() != null) {
                message += "Published on: " + schedule.getPublishedDate();
            }
            showAlert("Success", message);
            
        } catch (Exception e) {
            statusLabel.setText("Error publishing schedule: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to publish schedule: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Collect Fees button click.
     */
    @FXML
    private void handleCollectFees() {
        System.out.println("=== Collect Fees Action ===");
        
        try {
            // Call the organizer's collectFees method
            if (currentOrganizer != null) {
                currentOrganizer.collectFees();
            }
            
            // Log the action
            logActivity("Collected fees from teams");
            
            statusLabel.setText("Fee collection initiated");
            System.out.println("✓ Fee collection processed");
            
            // Show information
            showAlert("Fee Collection", "Fee collection process initiated.\n\n" +
                     "Sample fee status:\n" +
                     "- Team Alpha: Paid\n" +
                     "- Team Beta: Pending\n" +
                     "- Team Gamma: Paid\n" +
                     "- Team Delta: Pending");
            
        } catch (Exception e) {
            statusLabel.setText("Error collecting fees: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to collect fees: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Find Sponsor button click.
     */
    @FXML
    private void handleFindSponsor() {
        System.out.println("=== Find Sponsor Action ===");
        
        try {
            // Call the organizer's findSponsor method
            if (currentOrganizer != null) {
                currentOrganizer.findSponsor();
            }
            
            // Log the action
            logActivity("Finding sponsors for tournament");
            
            statusLabel.setText("Sponsor search initiated");
            System.out.println("✓ Sponsor search processed");
            
            // Show information
            showAlert("Find Sponsor", "Sponsor search process initiated.\n\n" +
                     "Potential sponsors:\n" +
                     "- Sports Inc.\n" +
                     "- Athletic Gear Co.\n" +
                     "- Tournament Partners LLC\n" +
                     "- Championship Sponsors");
            
        } catch (Exception e) {
            statusLabel.setText("Error finding sponsor: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to find sponsor: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Check Schedule button click.
     */
    @FXML
    private void handleCheckSchedule() {
        System.out.println("=== Check Schedule Action ===");
        
        try {
            // Call the organizer's checkSchedule method
            if (currentOrganizer != null) {
                currentOrganizer.checkSchedule();
            }
            
            // Log the action
            logActivity("Checked tournament schedule");
            
            statusLabel.setText("Schedule validation complete");
            System.out.println("✓ Schedule checked");
            
            // Show information
            showAlert("Schedule Status", "Tournament schedule has been validated.\n\n" +
                     "Schedule Status:\n" +
                     "✓ All matches scheduled\n" +
                     "✓ No venue conflicts\n" +
                     "✓ All referees assigned\n" +
                     "✓ Ready for publication");
            
        } catch (Exception e) {
            statusLabel.setText("Error checking schedule: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to check schedule: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Assign Referees button click.
     */
    @FXML
    private void handleAssignReferees() {
        System.out.println("=== Assign Referees Action ===");
        
        try {
            // Call the organizer's assignReferees method
            if (currentOrganizer != null) {
                currentOrganizer.assignReferees();
            }
            
            // Log the action
            logActivity("Assigned referees to matches");
            
            statusLabel.setText("Referees assigned successfully!");
            System.out.println("✓ Referees assigned");
            
            // Show confirmation
            showAlert("Success", "Referees have been assigned to tournament matches!\n\n" +
                     "Sample assignments:\n" +
                     "- Match 1: Referee John Smith\n" +
                     "- Match 2: Referee Jane Doe\n" +
                     "- Match 3: Referee Mike Johnson");
            
        } catch (Exception e) {
            statusLabel.setText("Error assigning referees: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to assign referees: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Postpone Match button click.
     */
    @FXML
    private void handlePostponeMatch() {
        System.out.println("=== Postpone Match Action ===");
        
        try {
            // Call the organizer's postponeMatch method
            if (currentOrganizer != null) {
                currentOrganizer.postponeMatch();
            }
            
            // Log the action
            logActivity("Postponed a match");
            
            statusLabel.setText("Match postponed successfully!");
            System.out.println("✓ Match postponed");
            
            // Show confirmation
            showAlert("Success", "Match has been postponed.\n\n" +
                     "The affected teams will be notified and a new date will be scheduled.");
            
        } catch (Exception e) {
            statusLabel.setText("Error postponing match: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to postpone match: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Show Request Menu button click.
     */
    @FXML
    private void handleShowRequestMenu() {
        System.out.println("=== Show Request Menu Action ===");
        
        try {
            // Call the organizer's showRequestMenuPopUp method
            if (currentOrganizer != null) {
                currentOrganizer.showRequestMenuPopUp();
            }
            
            // Log the action
            logActivity("Opened request menu");
            
            statusLabel.setText("Request menu displayed");
            System.out.println("✓ Request menu shown");
            
            // Show request menu
            showAlert("Request Menu", "Available Requests:\n\n" +
                     "- Team registration requests\n" +
                     "- Schedule change requests\n" +
                     "- Referee assignment requests\n" +
                     "- Venue change requests\n" +
                     "- Postponement requests");
            
        } catch (Exception e) {
            statusLabel.setText("Error showing request menu: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to show request menu: " + e.getMessage());
        }
    }
    
    /**
     * Logs an activity to the activity log area.
     */
    private void logActivity(String message) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        String logEntry = "[" + timestamp + "] " + message + "\n";
        activityLogArea.appendText(logEntry);
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") || title.toLowerCase().contains("fail") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Sets the current user (called from login).
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("Current user set: " + user.getUsername() + " with role: " + user.getRole());
        
        // Update welcome label if it exists
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + " (Tournament Organizer)");
        }
        
        // If the user is a TournamentOrganizer, set as currentOrganizer
        if (user instanceof TournamentOrganizer) {
            this.currentOrganizer = (TournamentOrganizer) user;
            logActivity("Tournament Organizer " + user.getUsername() + " logged in");
        }
    }
    
    /**
     * Handles logout button click.
     */
    @FXML
    private void handleLogout() {
        try {
            if (currentOrganizer != null) {
                logActivity("Tournament Organizer " + currentOrganizer.getUsername() + " logged out");
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Tournament Management System - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to login screen: " + e.getMessage());
        }
    }
}
