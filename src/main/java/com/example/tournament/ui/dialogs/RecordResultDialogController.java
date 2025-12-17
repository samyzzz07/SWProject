package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.ScoringService;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.format.DateTimeFormatter;

/**
 * Controller for the Record Result Dialog.
 * Allows Game Coordinator to record match results for tournaments.
 */
public class RecordResultDialogController {
    
    @FXML private ComboBox<Tournament> tournamentComboBox;
    @FXML private ComboBox<Match> matchComboBox;
    @FXML private Label team1Label;
    @FXML private Label team2Label;
    @FXML private TextField team1ScoreField;
    @FXML private TextField team2ScoreField;
    @FXML private Label matchDetailsLabel;
    @FXML private Label statusLabel;
    @FXML private ListView<Match> completedMatchesListView;
    
    private TournamentService tournamentService;
    private ScoringService scoringService;
    private ObservableList<Tournament> tournaments;
    private ObservableList<Match> matches;
    
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        scoringService = new ScoringService();
        
        // Load tournaments
        loadTournaments();
        
        // Load completed matches
        loadCompletedMatches();
        
        // Set up tournament selection listener
        tournamentComboBox.setOnAction(e -> handleTournamentSelection());
        
        // Set up match selection listener
        matchComboBox.setOnAction(e -> handleMatchSelection());
        
        statusLabel.setText("Ready");
    }
    
    private void loadTournaments() {
        tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentComboBox.setItems(tournaments);
    }
    
    private void handleTournamentSelection() {
        Tournament selected = tournamentComboBox.getValue();
        if (selected != null) {
            EntityManager em = JPAUtil.getEntityManager();
            try {
                // Load matches for selected tournament
                TypedQuery<Match> query = em.createQuery(
                    "SELECT m FROM Match m WHERE m.tournament = :tournament AND m.status != 'COMPLETED' ORDER BY m.id",
                    Match.class
                );
                query.setParameter("tournament", selected);
                matches = FXCollections.observableArrayList(query.getResultList());
                matchComboBox.setItems(matches);
                
                // Custom cell factory for matches
                matchComboBox.setCellFactory(lv -> new ListCell<Match>() {
                    @Override
                    protected void updateItem(Match item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            String team1 = item.getTeam1() != null ? item.getTeam1().getName() : "TBD";
                            String team2 = item.getTeam2() != null ? item.getTeam2().getName() : "TBD";
                            String time = item.getScheduledTime() != null ? 
                                " (" + item.getScheduledTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) + ")" : "";
                            setText(team1 + " vs " + team2 + time);
                        }
                    }
                });
                matchComboBox.setButtonCell(matchComboBox.getCellFactory().call(null));
                
                // Clear previous selection
                clearMatchDetails();
            } finally {
                em.close();
            }
        }
    }
    
    private void handleMatchSelection() {
        Match selected = matchComboBox.getValue();
        if (selected != null) {
            // Display match details
            String team1Name = selected.getTeam1() != null ? selected.getTeam1().getName() : "TBD";
            String team2Name = selected.getTeam2() != null ? selected.getTeam2().getName() : "TBD";
            
            team1Label.setText(team1Name);
            team2Label.setText(team2Name);
            
            String details = "Match Status: " + selected.getStatus();
            if (selected.getScheduledTime() != null) {
                details += "\nScheduled: " + selected.getScheduledTime().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            if (selected.getVenue() != null) {
                details += "\nVenue: " + selected.getVenue().getName();
            }
            matchDetailsLabel.setText(details);
            
            // Pre-fill scores if they exist
            if (selected.getTeam1Score() != null) {
                team1ScoreField.setText(selected.getTeam1Score().toString());
            }
            if (selected.getTeam2Score() != null) {
                team2ScoreField.setText(selected.getTeam2Score().toString());
            }
        }
    }
    
    @FXML
    private void handleRecordResult() {
        Match selectedMatch = matchComboBox.getValue();
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to record results.", Alert.AlertType.WARNING);
            return;
        }
        
        if (team1ScoreField.getText().isEmpty() || team2ScoreField.getText().isEmpty()) {
            showAlert("Incomplete Score Information", "Please enter scores for both teams.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            int team1Score = Integer.parseInt(team1ScoreField.getText());
            int team2Score = Integer.parseInt(team2ScoreField.getText());
            
            if (team1Score < 0 || team2Score < 0) {
                showAlert("Invalid Scores", "Scores cannot be negative.", Alert.AlertType.WARNING);
                return;
            }
            
            // Confirm before recording
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Result");
            confirm.setHeaderText("Record match result?");
            confirm.setContentText(team1Label.getText() + ": " + team1Score + "\n" +
                                  team2Label.getText() + ": " + team2Score);
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (scoringService.recordMatchResults(selectedMatch.getId(), team1Score, team2Score)) {
                        statusLabel.setText("Result recorded successfully!");
                        showAlert("Success", "Match result has been recorded and saved to the database.", 
                                 Alert.AlertType.INFORMATION);
                        
                        // Refresh data
                        loadCompletedMatches();
                        handleTournamentSelection(); // Reload matches for tournament
                        clearMatchDetails();
                    } else {
                        showAlert("Error", "Failed to record match result.", Alert.AlertType.ERROR);
                    }
                }
            });
            
        } catch (NumberFormatException e) {
            showAlert("Invalid Score Format", "Please enter valid numeric scores.", Alert.AlertType.WARNING);
        }
    }
    
    private void loadCompletedMatches() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.status = 'COMPLETED' ORDER BY m.id DESC",
                Match.class
            );
            query.setMaxResults(10); // Show only recent 10
            ObservableList<Match> completed = FXCollections.observableArrayList(query.getResultList());
            completedMatchesListView.setItems(completed);
            
            // Custom cell factory for completed matches
            completedMatchesListView.setCellFactory(lv -> new ListCell<Match>() {
                @Override
                protected void updateItem(Match item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String team1 = item.getTeam1() != null ? item.getTeam1().getName() : "TBD";
                        String team2 = item.getTeam2() != null ? item.getTeam2().getName() : "TBD";
                        String score = (item.getTeam1Score() != null ? item.getTeam1Score() : 0) + " - " +
                                      (item.getTeam2Score() != null ? item.getTeam2Score() : 0);
                        setText(team1 + " " + score + " " + team2);
                    }
                }
            });
        } finally {
            em.close();
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadTournaments();
        loadCompletedMatches();
        statusLabel.setText("Data refreshed");
    }
    
    @FXML
    private void handleClose() {
        Stage stage = (Stage) tournamentComboBox.getScene().getWindow();
        stage.close();
    }
    
    private void clearMatchDetails() {
        matchComboBox.setValue(null);
        team1Label.setText("-");
        team2Label.setText("-");
        team1ScoreField.clear();
        team2ScoreField.clear();
        matchDetailsLabel.setText("");
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
