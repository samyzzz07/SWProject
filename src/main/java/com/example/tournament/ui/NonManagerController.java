package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for NonManager interface.
 * NonManager can perform: ViewTournament, RegisterTeam, AddGames, ViewSchedule
 */
public class NonManagerController {
    
    @FXML private Label welcomeLabel;
    @FXML private ListView<Tournament> tournamentListView;
    @FXML private ListView<Match> scheduleListView;
    @FXML private TextField teamNameField;
    @FXML private TextField contactInfoField;
    @FXML private Button registerTeamButton;
    @FXML private Button addGameButton;
    @FXML private Label statusLabel;
    
    private User currentUser;
    private TournamentService tournamentService;
    private TeamService teamService;
    private GameService gameService;
    private ScheduleService scheduleService;
    
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        teamService = new TeamService();
        gameService = new GameService();
        scheduleService = new ScheduleService();
        
        statusLabel.setText("Ready");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (Non-Manager)");
        loadData();
    }
    
    private void loadData() {
        // ViewTournament
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentListView.setItems(tournaments);
        
        // ViewSchedule
        ObservableList<Match> schedule = FXCollections.observableArrayList(
            scheduleService.viewSchedule()
        );
        scheduleListView.setItems(schedule);
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
    private void handleAddGame() {
        statusLabel.setText("Add game functionality - opens dialog");
        showAlert("Add Game", "This would open a dialog to add a new game");
    }
    
    @FXML
    private void handleViewScheduleDetails() {
        Match selected = scheduleListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAlert("Match Details", selected.toString());
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
