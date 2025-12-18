package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.TeamService;
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
    
    @FXML
    private Button updateTeamButton;
    
    @FXML
    private Button saveTeamButton;
    
    @FXML
    private Button requestApprovalButton;
    
    @FXML
    private Button viewStatsButton;
    
    @FXML
    private TextField playerNameField;
    
    @FXML
    private TextField jerseyNumberField;
    
    @FXML
    private TextField positionField;
    
    @FXML
    private Button addPlayerButton;
    
    @FXML
    private TextField removePlayerIdField;
    
    @FXML
    private Button removePlayerButton;
    
    @FXML
    private ListView<Player> playerListView;
    
    @FXML
    private Label selectedTeamLabel;
    
    @FXML
    private Label playerCountLabel;
    
    @FXML
    private Button refreshPlayersButton;
    
    // Observable lists for UI binding
    private ObservableList<TimeSlot> timeSlots;
    private ObservableList<Team> teams;
    private ObservableList<Player> players;
    
    // Current user (can be any type of user)
    private User currentUser;
    
    // Current team manager (would be set from login session)
    private TeamManager currentManager;
    
    // Currently selected team for adding time slots
    private Team selectedTeam;
    
    // Service for team operations
    private TeamService teamService = new TeamService();
    
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
        players = FXCollections.observableArrayList();
        
        // Bind lists to UI components
        timeSlotListView.setItems(timeSlots);
        teamListView.setItems(teams);
        playerListView.setItems(players);
        
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
                    loadTeamPlayers(selectedTeam);
                    updateSelectedTeamLabel();
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
     * Handles the Add Player button click.
     */
    @FXML
    private void handleAddPlayer() {
        System.out.println("=== Add Player Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team before adding players.");
            return;
        }
        
        try {
            // Validate inputs
            if (playerNameField.getText().isEmpty()) {
                statusLabel.setText("Error: Player name is required.");
                showAlert("Missing Information", "Please enter a player name.");
                return;
            }
            
            if (jerseyNumberField.getText().isEmpty()) {
                statusLabel.setText("Error: Jersey number is required.");
                showAlert("Missing Information", "Please enter a jersey number.");
                return;
            }
            
            // Parse jersey number
            int jerseyNumber;
            try {
                jerseyNumber = Integer.parseInt(jerseyNumberField.getText());
            } catch (NumberFormatException e) {
                statusLabel.setText("Error: Invalid jersey number.");
                showAlert("Invalid Input", "Jersey number must be a valid number.");
                return;
            }
            
            // Create new player
            Player newPlayer = new Player(
                playerNameField.getText(),
                jerseyNumber,
                positionField.getText()
            );
            
            // Add player to selected team
            boolean added = selectedTeam.addPlayer(newPlayer);
            
            if (added) {
                // Update observable list
                players.add(newPlayer);
                updatePlayerCount();
                
                statusLabel.setText("Player added successfully!");
                System.out.println("✓ Player added: " + newPlayer.getName());
                
                // Clear input fields
                playerNameField.clear();
                jerseyNumberField.clear();
                positionField.clear();
                
                // Show confirmation
                showAlert("Success", "Player '" + newPlayer.getName() + "' has been added to " + selectedTeam.getName() + "!");
            } else {
                statusLabel.setText("Error: Failed to add player.");
                showAlert("Error", "Failed to add player. The player may already exist in the team.");
            }
            
        } catch (Exception e) {
            statusLabel.setText("Error adding player: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to add player: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Remove Player button click.
     */
    @FXML
    private void handleRemovePlayer() {
        System.out.println("=== Remove Player Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team before removing players.");
            return;
        }
        
        try {
            if (removePlayerIdField.getText().isEmpty()) {
                statusLabel.setText("Error: Player ID is required.");
                showAlert("Missing Information", "Please enter a player ID to remove.");
                return;
            }
            
            // Parse player ID
            int playerId;
            try {
                playerId = Integer.parseInt(removePlayerIdField.getText());
            } catch (NumberFormatException e) {
                statusLabel.setText("Error: Invalid player ID.");
                showAlert("Invalid Input", "Player ID must be a valid number.");
                return;
            }
            
            // Remove player from selected team
            boolean removed = selectedTeam.removePlayer(playerId);
            
            if (removed) {
                // Reload player list
                loadTeamPlayers(selectedTeam);
                
                statusLabel.setText("Player removed successfully!");
                System.out.println("✓ Player with ID " + playerId + " removed");
                
                // Clear input field
                removePlayerIdField.clear();
                
                // Show confirmation
                showAlert("Success", "Player has been removed from " + selectedTeam.getName() + "!");
            } else {
                statusLabel.setText("Error: Player not found.");
                showAlert("Error", "No player with ID " + playerId + " found in " + selectedTeam.getName() + ".");
            }
            
        } catch (Exception e) {
            statusLabel.setText("Error removing player: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            showAlert("Error", "Failed to remove player: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Refresh Players button click.
     */
    @FXML
    private void handleRefreshPlayers() {
        System.out.println("=== Refresh Players Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team to view its players.");
            return;
        }
        
        loadTeamPlayers(selectedTeam);
        statusLabel.setText("Player list refreshed!");
    }
    
    /**
     * Handles the Update Team button click.
     */
    @FXML
    private void handleUpdateTeam() {
        System.out.println("=== Update Team Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team to update.");
            return;
        }
        
        try {
            // Call UpdateTeam on the model
            selectedTeam.UpdateTeam();
            
            // Persist the updated team to the database
            boolean success = teamService.updateTeam(selectedTeam);
            
            if (success) {
                // Refresh the player list to show updated data
                loadTeamPlayers(selectedTeam);
                updatePlayerCount();
                
                statusLabel.setText("Team updated successfully!");
                showAlert("Success", "Team '" + selectedTeam.getName() + "' has been updated!");
            } else {
                statusLabel.setText("Error: Failed to persist team update.");
                showAlert("Error", "Failed to save team update to database.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error updating team: " + e.getMessage());
            showAlert("Error", "Failed to update team: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Save Team button click.
     */
    @FXML
    private void handleSaveTeam() {
        System.out.println("=== Save Team Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team to save.");
            return;
        }
        
        try {
            selectedTeam.SaveTeam();
            statusLabel.setText("Team saved successfully!");
            showAlert("Success", "Team '" + selectedTeam.getName() + "' has been saved!");
        } catch (Exception e) {
            statusLabel.setText("Error saving team: " + e.getMessage());
            showAlert("Error", "Failed to save team: " + e.getMessage());
        }
    }
    
    /**
     * Handles the Request Approval button click.
     */
    @FXML
    private void handleRequestApproval() {
        System.out.println("=== Request Approval Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team to request approval.");
            return;
        }
        
        try {
            // Validate team info before requesting approval
            if (!selectedTeam.ValidateInfo()) {
                statusLabel.setText("Error: Team information is invalid.");
                showAlert("Invalid Team Info", "Please ensure the team has a valid name before requesting approval.");
                return;
            }
            
            // Set approval status to PENDING
            selectedTeam.RequestApproval();
            
            // Persist the approval status to the database
            boolean success = teamService.updateTeam(selectedTeam);
            
            if (success) {
                statusLabel.setText("Approval request sent successfully!");
                showAlert("Success", "Approval request for team '" + selectedTeam.getName() + "' has been submitted!");
            } else {
                statusLabel.setText("Error: Failed to save approval request.");
                showAlert("Error", "Failed to save approval request to database.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error requesting approval: " + e.getMessage());
            showAlert("Error", "Failed to request approval: " + e.getMessage());
        }
    }
    
    /**
     * Handles the View Stats button click.
     */
    @FXML
    private void handleViewStats() {
        System.out.println("=== View Stats Action ===");
        
        if (selectedTeam == null) {
            statusLabel.setText("Error: Please select a team first.");
            showAlert("No Team Selected", "Please select a team to view its statistics.");
            return;
        }
        
        try {
            TeamStats stats = selectedTeam.getTeamStats();
            
            StringBuilder statsMessage = new StringBuilder();
            statsMessage.append("Team: ").append(selectedTeam.getName()).append("\n\n");
            statsMessage.append("Games Played: ").append(stats.getGamesPlayed()).append("\n");
            statsMessage.append("Wins: ").append(stats.getWins()).append("\n");
            statsMessage.append("Losses: ").append(stats.getLosses()).append("\n");
            statsMessage.append("Draws: ").append(stats.getDraws()).append("\n");
            statsMessage.append("Total Players: ").append(selectedTeam.getPlayers().size()).append("\n");
            
            statusLabel.setText("Displaying team statistics");
            showAlert("Team Statistics", statsMessage.toString());
        } catch (Exception e) {
            statusLabel.setText("Error viewing stats: " + e.getMessage());
            showAlert("Error", "Failed to view team statistics: " + e.getMessage());
        }
    }
    
    /**
     * Loads players for a selected team.
     */
    private void loadTeamPlayers(Team team) {
        System.out.println("Loading players for team: " + team.getName());
        players.clear();
        players.addAll(team.getPlayers());
        updatePlayerCount();
        System.out.println("Loaded " + players.size() + " players.");
    }
    
    /**
     * Updates the player count label.
     */
    private void updatePlayerCount() {
        if (playerCountLabel != null) {
            playerCountLabel.setText("Total Players: " + players.size());
        }
    }
    
    /**
     * Updates the selected team label.
     */
    private void updateSelectedTeamLabel() {
        if (selectedTeamLabel != null) {
            if (selectedTeam != null) {
                selectedTeamLabel.setText(selectedTeam.getName());
            } else {
                selectedTeamLabel.setText("No team selected");
            }
        }
    }
    
    /**
     * Adds a player to the selected team.
     * @param p the player to add
     * @return true if player was added successfully, false otherwise
     */
    public boolean addPlayer(Player p) {
        if (selectedTeam != null) {
            boolean added = selectedTeam.addPlayer(p);
            if (added) {
                players.add(p);
                updatePlayerCount();
            }
            return added;
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
