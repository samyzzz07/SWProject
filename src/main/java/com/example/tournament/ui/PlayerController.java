package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for Player interface.
 * Player can perform: ViewTournament, UpdateTeam, ViewSport, RegisterTeam
 */
public class PlayerController {
    
    @FXML private Label welcomeLabel;
    @FXML private ListView<Tournament> tournamentListView;
    @FXML private ListView<Sport> sportListView;
    @FXML private TextArea teamInfoTextArea;
    @FXML private TextField teamNameField;
    @FXML private TextField contactInfoField;
    @FXML private Button registerTeamButton;
    @FXML private Button updateTeamButton;
    @FXML private Label statusLabel;
    
    private User currentUser;
    private TournamentService tournamentService;
    private SportService sportService;
    private TeamService teamService;
    
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        sportService = new SportService();
        teamService = new TeamService();
        
        statusLabel.setText("Ready");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (Player)");
        loadData();
    }
    
    private void loadData() {
        // ViewTournament
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentListView.setItems(tournaments);
        
        // ViewSport
        ObservableList<Sport> sports = FXCollections.observableArrayList(
            sportService.viewAllSports()
        );
        sportListView.setItems(sports);
    }
    
    @FXML
    private void handleRegisterTeam() {
        String teamName = teamNameField.getText();
        String contactInfo = contactInfoField.getText();
        
        if (teamName.isEmpty()) {
            statusLabel.setText("Team name is required");
            return;
        }
        
        Team team = new Team(teamName);
        team.setContactInfo(contactInfo);
        
        if (teamService.registerTeam(team)) {
            statusLabel.setText("Team registered successfully!");
            teamNameField.clear();
            contactInfoField.clear();
        } else {
            statusLabel.setText("Failed to register team");
        }
    }
    
    @FXML
    private void handleUpdateTeam() {
        String teamInfo = teamInfoTextArea.getText();
        
        if (teamInfo.isEmpty()) {
            statusLabel.setText("Please enter team information to update");
            return;
        }
        
        // In a real implementation, this would:
        // 1. Retrieve the current user's team from the database
        // 2. Update the team information
        // 3. Call teamService.updateTeam(team) to persist changes
        
        // For demonstration, we just show a message
        statusLabel.setText("Team information updated");
        showAlert("Team Update", "Team information has been updated: " + teamInfo);
    }
    
    @FXML
    private void handleViewTournamentDetails() {
        Tournament selected = tournamentListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAlert("Tournament Details", selected.toString());
        }
    }
    
    @FXML
    private void handleViewSportDetails() {
        Sport selected = sportListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAlert("Sport Details", selected.toString());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") || title.toLowerCase().contains("fail") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
