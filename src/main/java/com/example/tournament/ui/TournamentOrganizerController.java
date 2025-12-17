package com.example.tournament.ui;

import com.example.tournament.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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
            logActivity("Opened View Registered Teams dialog");
            
            statusLabel.setText("Displaying registered teams");
            System.out.println("✓ Opening View Teams dialog");
            
            // Open the dialog
            openDialog("/fxml/view_teams_dialog.fxml", "Registered Teams", 650, 600);
            
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
            logActivity("Opened Approve Team dialog");
            
            statusLabel.setText("Approve team dialog opened");
            System.out.println("✓ Opening Approve Team dialog");
            
            // Open the dialog
            openDialog("/fxml/approve_team_dialog.fxml", "Approve Team", 550, 550);
            
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
            logActivity("Opened Publish Schedule dialog");
            
            statusLabel.setText("Publish schedule dialog opened");
            System.out.println("✓ Opening Publish Schedule dialog");
            
            // Open the dialog
            openDialog("/fxml/publish_schedule_dialog.fxml", "Publish Schedule", 650, 650);
            
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
            logActivity("Opened Collect Fees dialog");
            
            statusLabel.setText("Fee collection dialog opened");
            System.out.println("✓ Opening Collect Fees dialog");
            
            // Open the dialog
            openDialog("/fxml/collect_fees_dialog.fxml", "Fee Collection Management", 650, 600);
            
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
            logActivity("Opened Find Sponsor dialog");
            
            statusLabel.setText("Sponsor management dialog opened");
            System.out.println("✓ Opening Find Sponsor dialog");
            
            // Open the dialog
            openDialog("/fxml/find_sponsor_dialog.fxml", "Sponsor Management", 550, 650);
            
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
            logActivity("Opened Check Schedule dialog");
            
            statusLabel.setText("Schedule validation dialog opened");
            System.out.println("✓ Opening Check Schedule dialog");
            
            // Open the dialog
            openDialog("/fxml/check_schedule_dialog.fxml", "Schedule Validation", 700, 650);
            
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
            logActivity("Opened Assign Referees dialog");
            
            statusLabel.setText("Assign referees dialog opened");
            System.out.println("✓ Opening Assign Referees dialog");
            
            // Open the dialog
            openDialog("/fxml/assign_referees_dialog.fxml", "Assign Referees to Matches", 550, 550);
            
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
            logActivity("Opened Postpone Match dialog");
            
            statusLabel.setText("Postpone match dialog opened");
            System.out.println("✓ Opening Postpone Match dialog");
            
            // Open the dialog
            openDialog("/fxml/postpone_match_dialog.fxml", "Postpone Match", 550, 650);
            
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
            
            statusLabel.setText("Request menu opened");
            System.out.println("✓ Opening Request Menu dialog");
            
            // Open the dialog
            openDialog("/fxml/request_menu_dialog.fxml", "Tournament Requests Management", 700, 650);
            
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
     * Opens a dialog window with the specified FXML file.
     * 
     * @param fxmlPath the path to the FXML file
     * @param title the title of the dialog window
     * @param width the width of the dialog window
     * @param height the height of the dialog window
     */
    private void openDialog(String fxmlPath, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initOwner(statusLabel.getScene().getWindow());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(root, width, height));
            dialogStage.setResizable(false);
            dialogStage.show();
            
            System.out.println("✓ Dialog opened: " + title);
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open dialog: " + e.getMessage());
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
            stage.setMaximized(true);  // Maintain maximized mode
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to login screen: " + e.getMessage());
        }
    }
}
