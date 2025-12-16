package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for Referee interface.
 * Referee can perform: PostScores
 */
public class RefereeController {
    
    @FXML private Label welcomeLabel;
    @FXML private ListView<Match> matchListView;
    @FXML private TextField team1ScoreField;
    @FXML private TextField team2ScoreField;
    @FXML private Button postScoreButton;
    @FXML private Label statusLabel;
    @FXML private TextArea matchDetailsArea;
    
    private User currentUser;
    private ScoringService scoringService;
    private GameService gameService;
    
    @FXML
    public void initialize() {
        scoringService = new ScoringService();
        gameService = new GameService();
        
        statusLabel.setText("Ready");
        
        // Add listener to show match details when selected
        matchListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayMatchDetails(newValue);
                }
            }
        );
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (Referee)");
        loadData();
    }
    
    private void loadData() {
        // Load all matches
        ObservableList<Match> matches = FXCollections.observableArrayList(
            gameService.viewAllGames()
        );
        matchListView.setItems(matches);
    }
    
    private void displayMatchDetails(Match match) {
        StringBuilder details = new StringBuilder();
        details.append("Match ID: ").append(match.getId()).append("\n");
        details.append("Team 1: ").append(match.getTeam1() != null ? match.getTeam1().getName() : "TBD").append("\n");
        details.append("Team 2: ").append(match.getTeam2() != null ? match.getTeam2().getName() : "TBD").append("\n");
        details.append("Scheduled Time: ").append(match.getScheduledTime()).append("\n");
        details.append("Status: ").append(match.getStatus()).append("\n");
        details.append("Current Score: ");
        if (match.getTeam1Score() != null && match.getTeam2Score() != null) {
            details.append(match.getTeam1Score()).append(" - ").append(match.getTeam2Score());
        } else {
            details.append("Not yet posted");
        }
        
        matchDetailsArea.setText(details.toString());
    }
    
    @FXML
    private void handlePostScore() {
        Match selected = matchListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a match first");
            showAlert("Error", "Please select a match to post scores");
            return;
        }
        
        String team1ScoreText = team1ScoreField.getText();
        String team2ScoreText = team2ScoreField.getText();
        
        if (team1ScoreText.isEmpty() || team2ScoreText.isEmpty()) {
            statusLabel.setText("Please enter both team scores");
            showAlert("Error", "Please enter scores for both teams");
            return;
        }
        
        try {
            int team1Score = Integer.parseInt(team1ScoreText);
            int team2Score = Integer.parseInt(team2ScoreText);
            
            if (scoringService.postScores(selected.getId(), team1Score, team2Score)) {
                statusLabel.setText("Scores posted successfully!");
                showAlert("Success", "Match scores have been posted successfully!");
                team1ScoreField.clear();
                team2ScoreField.clear();
                loadData(); // Refresh match list
            } else {
                statusLabel.setText("Failed to post scores");
                showAlert("Error", "Failed to post scores. Please try again.");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid score values");
            showAlert("Error", "Please enter valid numeric scores");
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadData();
        statusLabel.setText("Match list refreshed");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
