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
 * JavaFX Controller for administrator dashboard.
 */
public class AdministratorController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField tournamentNameField;
    
    @FXML
    private ComboBox<String> rulesComboBox;
    
    @FXML
    private ComboBox<String> reportComboBox;
    
    @FXML
    private ComboBox<String> endTournamentComboBox;
    
    @FXML
    private Button createTournamentButton;
    
    @FXML
    private Button defineRulesButton;
    
    @FXML
    private Button viewReportButton;
    
    @FXML
    private Button endTournamentButton;
    
    @FXML
    private TextArea activityLogArea;
    
    // Current user
    private User currentUser;
    private Administrator currentAdmin;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("Initializing AdministratorController...");
        
        // Initialize combo boxes with placeholder data
        rulesComboBox.getItems().addAll("Tournament A", "Tournament B", "Tournament C");
        reportComboBox.getItems().addAll("Tournament A", "Tournament B", "Tournament C");
        endTournamentComboBox.getItems().addAll("Tournament A", "Tournament B", "Tournament C");
        
        statusLabel.setText("Ready");
        activityLogArea.setText("Administrator Dashboard initialized.\n");
        
        System.out.println("Initialization complete.");
    }
    
    /**
     * Handles the Create Tournament button click.
     */
    @FXML
    private void handleCreateTournament() {
        System.out.println("=== Create Tournament Action ===");
        
        try {
            if (tournamentNameField.getText().isEmpty()) {
                statusLabel.setText("Error: Tournament name is required.");
                showAlert("Missing Information", "Please enter a tournament name.");
                return;
            }
            
            String tournamentName = tournamentNameField.getText();
            
            // Call the administrator's createTournament method
            if (currentAdmin != null) {
                currentAdmin.createTournament();
            }
            
            // Log the action
            logActivity("Created tournament: " + tournamentName);
            
            statusLabel.setText("Tournament created successfully!");
            System.out.println("✓ Tournament created: " + tournamentName);
            
            // Add to combo boxes for other operations
            rulesComboBox.getItems().add(tournamentName);
            reportComboBox.getItems().add(tournamentName);
            endTournamentComboBox.getItems().add(tournamentName);
            
            // Clear input field
            tournamentNameField.clear();
            
            // Show confirmation
            showAlert("Success", "Tournament '" + tournamentName + "' has been created successfully!");
            
        } catch (Exception e) {
            statusLabel.setText("Error creating tournament: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to create tournament: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Define Tournament Rules button click.
     */
    @FXML
    private void handleDefineTournamentRules() {
        System.out.println("=== Define Tournament Rules Action ===");
        
        try {
            if (rulesComboBox.getValue() == null) {
                statusLabel.setText("Error: Please select a tournament.");
                showAlert("Missing Information", "Please select a tournament.");
                return;
            }
            
            String tournamentName = rulesComboBox.getValue();
            
            // Call the administrator's defineTournamentRules method
            if (currentAdmin != null) {
                currentAdmin.defineTournamentRules();
            }
            
            // Log the action
            logActivity("Defined rules for tournament: " + tournamentName);
            
            statusLabel.setText("Tournament rules defined successfully!");
            System.out.println("✓ Tournament rules defined for: " + tournamentName);
            
            // Show confirmation with some sample rules
            showAlert("Success", "Tournament rules have been defined for '" + tournamentName + "'.\n\n" +
                     "Sample rules:\n" +
                     "- Match duration: 90 minutes\n" +
                     "- Teams per group: 4\n" +
                     "- Points for win: 3\n" +
                     "- Points for draw: 1");
            
        } catch (Exception e) {
            statusLabel.setText("Error defining rules: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to define tournament rules: " + e.getMessage());
        }
    }
    
    /**
     * Handles the View Tournament Report button click.
     */
    @FXML
    private void handleViewTournamentReport() {
        System.out.println("=== View Tournament Report Action ===");
        
        try {
            if (reportComboBox.getValue() == null) {
                statusLabel.setText("Error: Please select a tournament.");
                showAlert("Missing Information", "Please select a tournament.");
                return;
            }
            
            String tournamentName = reportComboBox.getValue();
            
            // Call the administrator's viewTournamentReport method
            if (currentAdmin != null) {
                currentAdmin.viewTournamentReport();
            }
            
            // Log the action
            logActivity("Viewed report for tournament: " + tournamentName);
            
            statusLabel.setText("Tournament report displayed successfully!");
            System.out.println("✓ Tournament report viewed for: " + tournamentName);
            
            // Show sample report
            showAlert("Tournament Report", "Report for: " + tournamentName + "\n\n" +
                     "Status: Active\n" +
                     "Total Teams: 16\n" +
                     "Matches Played: 24\n" +
                     "Matches Remaining: 8\n" +
                     "Total Goals: 67\n" +
                     "Average Attendance: 1,250");
            
        } catch (Exception e) {
            statusLabel.setText("Error viewing report: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to view tournament report: " + e.getMessage());
        }
    }
    
    /**
     * Handles the End Tournament button click.
     */
    @FXML
    private void handleEndTournament() {
        System.out.println("=== End Tournament Action ===");
        
        try {
            if (endTournamentComboBox.getValue() == null) {
                statusLabel.setText("Error: Please select a tournament.");
                showAlert("Missing Information", "Please select a tournament.");
                return;
            }
            
            String tournamentName = endTournamentComboBox.getValue();
            
            // Confirm action
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm End Tournament");
            confirmAlert.setHeaderText("End Tournament: " + tournamentName);
            confirmAlert.setContentText("Are you sure you want to end this tournament? This action cannot be undone.");
            
            if (confirmAlert.showAndWait().get() != ButtonType.OK) {
                statusLabel.setText("Tournament end cancelled.");
                return;
            }
            
            // Call the administrator's endTournament method
            if (currentAdmin != null) {
                currentAdmin.endTournament();
            }
            
            // Log the action
            logActivity("Ended tournament: " + tournamentName);
            
            statusLabel.setText("Tournament ended successfully!");
            System.out.println("✓ Tournament ended: " + tournamentName);
            
            // Remove from combo boxes
            rulesComboBox.getItems().remove(tournamentName);
            reportComboBox.getItems().remove(tournamentName);
            endTournamentComboBox.getItems().remove(tournamentName);
            
            // Show confirmation
            showAlert("Success", "Tournament '" + tournamentName + "' has been ended successfully!");
            
        } catch (Exception e) {
            statusLabel.setText("Error ending tournament: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to end tournament: " + e.getMessage());
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
            welcomeLabel.setText("Welcome, " + user.getUsername() + " (Administrator)");
        }
        
        // If the user is an Administrator, set as currentAdmin
        if (user instanceof Administrator) {
            this.currentAdmin = (Administrator) user;
            logActivity("Administrator " + user.getUsername() + " logged in");
        }
    }
    
    /**
     * Handles logout button click.
     */
    @FXML
    private void handleLogout() {
        try {
            if (currentAdmin != null) {
                logActivity("Administrator " + currentAdmin.getUsername() + " logged out");
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
