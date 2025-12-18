package com.example.tournament.ui;

import com.example.tournament.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JavaFX Controller for participant team management.
 */
public class ParticipantTeamController {
    
    @FXML
    private TextField teamNameField;
    
    @FXML
    private TextField contactInfoField;
    
    @FXML
    private DatePicker timeSlotDatePicker;
    
    @FXML
    private TextField startTimeField;
    
    @FXML
    private TextField endTimeField;
    
    @FXML
    private ListView<TimeSlot> timeSlotListView;
    
    @FXML
    private ListView<Team> teamListView;
    
    @FXML
    private Button addTimeSlotButton;
    
    @FXML
    private Button registerTeamButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private ComboBox<String> sportComboBox;
    
    @FXML
    private Label welcomeLabel;
    
    // Observable lists for UI binding
    private ObservableList<TimeSlot> timeSlots;
    private ObservableList<Team> teams;
    
    // Current user (can be any type of user)
    private User currentUser;
    
    // Current team manager (would be set from login session)
    private TeamManager currentManager;
    
    // Currently selected team for adding time slots
    private Team selectedTeam;
    
    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("Initializing ParticipantTeamController...");
        
        // Initialize observable lists
        timeSlots = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();
        
        // Bind lists to UI components
        timeSlotListView.setItems(timeSlots);
        teamListView.setItems(teams);
        
        // Set up sport combo box with placeholder values
        ObservableList<String> sports = FXCollections.observableArrayList(
            "Football", "Basketball", "Tennis", "Volleyball", "Cricket"
        );
        sportComboBox.setItems(sports);
        
        // Set up selection listeners
        teamListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedTeam = newValue;
                if (selectedTeam != null) {
                    System.out.println("Selected team: " + selectedTeam.getName());
                    loadTeamTimeSlots(selectedTeam);
                }
            }
        );
        
        // Initialize with placeholder team manager
        // In a real application, this would come from login session
        currentManager = new TeamManager("demo_manager", "password", "manager@example.com");
        
        statusLabel.setText("Ready");
        System.out.println("Initialization complete.");
    }
    
    /**
     * Handles the Add Time Slot button click.
     */
    @FXML
    private void handleAddTimeSlotButton() {
        System.out.println("=== Add Time Slot Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team before adding time slots.");
            return;
        }
        
        try {
            // Parse time slot data from input fields
            if (timeSlotDatePicker.getValue() == null || 
                startTimeField.getText().isEmpty() || 
                endTimeField.getText().isEmpty()) {
                statusLabel.setText("Error: Please fill all time slot fields.");
                showAlert("Missing Information", "Please fill in date, start time, and end time.");
                return;
            }
            
            // Create time slot
            // In a real app, would parse time from startTimeField and endTimeField
            LocalDateTime startTime = timeSlotDatePicker.getValue().atTime(9, 0);
            LocalDateTime endTime = timeSlotDatePicker.getValue().atTime(11, 0);
            
            TimeSlot newTimeSlot = new TimeSlot(startTime, endTime);
            
            // Add to team's preferred time slots
            selectedTeam.addPreferredTimeSlot(newTimeSlot);
            
            // Update observable list
            timeSlots.add(newTimeSlot);
            
            // Use team manager to manage time slots
            currentManager.manageTeamTimeSlots(selectedTeam, newTimeSlot);
            
            statusLabel.setText("Time slot added successfully!");
            System.out.println("✓ Time slot added: " + newTimeSlot);
            
            // Clear input fields
            startTimeField.clear();
            endTimeField.clear();
            
        } catch (Exception e) {
            statusLabel.setText("Error adding time slot: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to add time slot: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Register Team button click.
     */
    @FXML
    private void handleRegisterTeamButton() {
        System.out.println("=== Register Team Action ===");
        
        try {
            // Validate inputs
            if (teamNameField.getText().isEmpty()) {
                statusLabel.setText("Error: Team name is required.");
                showAlert("Missing Information", "Please enter a team name.");
                return;
            }
            
            // Create new team
            Team newTeam = new Team(teamNameField.getText());
            newTeam.setContactInfo(contactInfoField.getText());
            newTeam.setManager(currentManager);
            
            // Register team through team manager
            currentManager.registerTeam(newTeam);
            
            // Add to observable list for UI
            teams.add(newTeam);
            
            statusLabel.setText("Team registered successfully!");
            System.out.println("✓ Team registered: " + newTeam.getName());
            
            // Clear input fields
            teamNameField.clear();
            contactInfoField.clear();
            
            // Show confirmation
            showAlert("Success", "Team '" + newTeam.getName() + "' has been registered successfully!");
            
        } catch (Exception e) {
            statusLabel.setText("Error registering team: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to register team: " + e.getMessage());
        }
    }
    
    /**
     * Loads time slot preferences for a selected team.
     */
    private void loadTeamTimeSlots(Team team) {
        System.out.println("Loading time slots for team: " + team.getName());
        timeSlots.clear();
        timeSlots.addAll(team.getPreferredTimeSlots());
        System.out.println("Loaded " + timeSlots.size() + " time slots.");
    }
    
    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Sets the current user (called from login).
     * This is a generic method that works for all user types.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("Current user set: " + user.getUsername() + " with role: " + user.getRole());
        
        // Update welcome label if it exists
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        }
        
        // If the user is a TeamManager, also set as currentManager
        if (user instanceof TeamManager) {
            this.currentManager = (TeamManager) user;
            // Load teams in a background thread to avoid blocking the UI
            new Thread(() -> {
                try {
                    loadTeamsForManager(currentManager);
                } catch (Exception e) {
                    System.err.println("Error loading teams in background: " + e.getMessage());
                }
            }).start();
        }
    }
    
    /**
     * Loads teams for the given team manager.
     */
    private void loadTeamsForManager(TeamManager manager) {
        try {
            // Check if teams is initialized before accessing
            if (manager.getTeams() != null) {
                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    teams.clear();
                    teams.addAll(manager.getTeams());
                    System.out.println("Loaded " + teams.size() + " teams for manager: " + manager.getUsername());
                });
            }
        } catch (Exception e) {
            System.err.println("Error loading teams: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes the team list.
     */
    public void refreshTeamList() {
        if (currentManager != null) {
            loadTeamsForManager(currentManager);
        }
    }
    
    /**
     * Handles logout button click.
     */
    @FXML
    private void handleLogout() {
        try {
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
     * Adds a player to the selected team.
     * @param p the player to add
     * @return true if player was added successfully, false otherwise
     */
    public boolean addPlayer(Player p) {
        if (selectedTeam != null) {
            return selectedTeam.addPlayer(p);
        }
        return false;
    }
    
    /**
     * Removes a player from the selected team by player ID.
     * @param playerId the ID of the player to remove
     * @return true if player was removed successfully, false otherwise
     */
    public boolean removePlayer(int playerId) {
        if (selectedTeam != null) {
            return selectedTeam.removePlayer(playerId);
        }
        return false;
    }
    
    /**
     * Gets the list of players in the selected team.
     * @return list of players, or null if no team is selected
     */
    public List<Player> getPlayers() {
        if (selectedTeam != null) {
            return selectedTeam.getPlayers();
        }
        return null;
    }
    
    /**
     * Gets the team statistics for the selected team.
     * @return team statistics, or null if no team is selected
     */
    public TeamStats getTeamStats() {
        if (selectedTeam != null) {
            return selectedTeam.getTeamStats();
        }
        return null;
    }
    
    /**
     * Checks if the selected team has a player with the given ID.
     * @param playerId the ID of the player to check
     * @return true if player exists in team, false otherwise
     */
    public boolean hasPlayer(int playerId) {
        if (selectedTeam != null) {
            return selectedTeam.hasPlayer(playerId);
        }
        return false;
    }
    
    /**
     * Validates the selected team information.
     * @return true if team info is valid, false otherwise
     */
    public boolean ValidateInfo() {
        if (selectedTeam != null) {
            return selectedTeam.ValidateInfo();
        }
        return false;
    }
    
    /**
     * Saves the selected team.
     */
    public void SaveTeam() {
        if (selectedTeam != null) {
            selectedTeam.SaveTeam();
        }
    }
    
    /**
     * Requests approval for the selected team.
     */
    public void RequestApproval() {
        if (selectedTeam != null) {
            selectedTeam.RequestApproval();
        }
    }
    
    /**
     * Updates the selected team information.
     */
    public void UpdateTeam() {
        if (selectedTeam != null) {
            selectedTeam.UpdateTeam();
        }
    }
}
