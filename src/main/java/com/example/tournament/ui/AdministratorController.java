package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * JavaFX Controller for administrator dashboard.
 */
public class AdministratorController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label statusLabel;
    
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
        
        // Load tournaments from database
        refreshTournamentLists();
        
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
            // Open the Create Tournament Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_tournament_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Tournament");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
            // Refresh tournament lists after dialog closes
            refreshTournamentLists();
            
            logActivity("Opened Create Tournament dialog");
            statusLabel.setText("Create Tournament dialog closed.");
            
        } catch (Exception e) {
            statusLabel.setText("Error opening Create Tournament dialog: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open Create Tournament dialog: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Define Tournament Rules button click.
     */
    @FXML
    private void handleDefineTournamentRules() {
        System.out.println("=== Define Tournament Rules Action ===");
        
        try {
            // Open the Define Tournament Rules Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/define_tournament_rules_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Define Tournament Rules");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
            logActivity("Opened Define Tournament Rules dialog");
            statusLabel.setText("Define Tournament Rules dialog closed.");
            
        } catch (Exception e) {
            statusLabel.setText("Error opening Define Rules dialog: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open Define Rules dialog: " + e.getMessage());
        }
    }
    
    /**
     * Handles the View Tournament Report button click.
     */
    @FXML
    private void handleViewTournamentReport() {
        System.out.println("=== View Tournament Report Action ===");
        
        try {
            // Open the View Tournament Report Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view_tournament_report_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("View Tournament Report");
            dialogStage.setScene(new Scene(root, 700, 700));
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            dialogStage.showAndWait();
            
            logActivity("Opened View Tournament Report dialog");
            statusLabel.setText("View Tournament Report dialog closed.");
            
        } catch (Exception e) {
            statusLabel.setText("Error opening View Report dialog: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open View Report dialog: " + e.getMessage());
        }
    }
    
    /**
     * Handles the End Tournament button click.
     */
    @FXML
    private void handleEndTournament() {
        System.out.println("=== End Tournament Action ===");
        
        try {
            // Open the End Tournament Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/end_tournament_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("End Tournament");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait();
            
            // Refresh tournament lists after dialog closes
            refreshTournamentLists();
            
            logActivity("Opened End Tournament dialog");
            statusLabel.setText("End Tournament dialog closed.");
            
        } catch (Exception e) {
            statusLabel.setText("Error opening End Tournament dialog: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open End Tournament dialog: " + e.getMessage());
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
            stage.setMaximized(true);  // Maintain maximized mode
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to login screen: " + e.getMessage());
        }
    }
    
    /**
     * Refreshes tournament lists in combo boxes.
     */
    private void refreshTournamentLists() {
        try {
            TournamentService tournamentService = new TournamentService();
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            
            System.out.println("Loaded " + tournaments.size() + " tournaments from database");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error refreshing tournament lists: " + e.getMessage());
        }
    }
}
