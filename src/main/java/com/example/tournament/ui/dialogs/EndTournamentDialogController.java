package com.example.tournament.ui.dialogs;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Controller for the End Tournament Dialog.
 */
public class EndTournamentDialogController {
    
    @FXML
    private ComboBox<String> tournamentComboBox;
    
    @FXML
    private Label tournamentNameLabel;
    
    @FXML
    private Label sportLabel;
    
    @FXML
    private Label typeLabel;
    
    @FXML
    private Label currentStatusLabel;
    
    @FXML
    private Label totalTeamsLabel;
    
    @FXML
    private Label totalMatchesLabel;
    
    @FXML
    private Label completedMatchesLabel;
    
    @FXML
    private TextArea finalNotesArea;
    
    @FXML
    private Label statusLabel;
    
    private TournamentService tournamentService;
    private Tournament selectedTournament;
    
    /**
     * Initialize the dialog.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        loadTournaments();
    }
    
    /**
     * Load tournaments from the database.
     */
    private void loadTournaments() {
        try {
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            ObservableList<String> tournamentNames = FXCollections.observableArrayList();
            
            // Only show tournaments that are not already completed
            for (Tournament tournament : tournaments) {
                if (tournament.getStatus() != TournamentStatus.COMPLETED) {
                    tournamentNames.add(tournament.getName());
                }
            }
            
            tournamentComboBox.setItems(tournamentNames);
            
            if (tournamentNames.isEmpty()) {
                statusLabel.setText("No active tournaments found.");
                statusLabel.setStyle("-fx-text-fill: #666;");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading tournaments");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    /**
     * Handle tournament selection.
     */
    @FXML
    private void handleTournamentSelection() {
        String selectedName = tournamentComboBox.getValue();
        if (selectedName == null) {
            return;
        }
        
        try {
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            selectedTournament = tournaments.stream()
                .filter(t -> t.getName().equals(selectedName))
                .findFirst()
                .orElse(null);
            
            if (selectedTournament != null) {
                // Update tournament details
                tournamentNameLabel.setText(selectedTournament.getName());
                sportLabel.setText(selectedTournament.getSport() != null ? 
                    selectedTournament.getSport().getName() : "N/A");
                
                if (selectedTournament instanceof LeagueTournament) {
                    typeLabel.setText("League/Round Robin");
                } else if (selectedTournament instanceof RoundRobinTournament) {
                    typeLabel.setText("Round Robin");
                } else if (selectedTournament instanceof KnockoutTournament) {
                    typeLabel.setText("Knockout");
                } else {
                    typeLabel.setText("Unknown");
                }
                
                currentStatusLabel.setText(selectedTournament.getStatus() != null ? 
                    selectedTournament.getStatus().toString() : "UNKNOWN");
                
                totalTeamsLabel.setText(String.valueOf(selectedTournament.getTeams().size()));
                totalMatchesLabel.setText(String.valueOf(selectedTournament.getMatches().size()));
                
                int completedMatches = (int) selectedTournament.getMatches().stream()
                    .filter(m -> m.getStatus() == Match.MatchStatus.COMPLETED)
                    .count();
                completedMatchesLabel.setText(String.valueOf(completedMatches));
                
                statusLabel.setText("Tournament selected. Review details before ending.");
                statusLabel.setStyle("-fx-text-fill: #666;");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading tournament details");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    /**
     * Handle end tournament button click.
     */
    @FXML
    private void handleEndTournament() {
        if (selectedTournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament to end.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm the action
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm End Tournament");
        confirmAlert.setHeaderText("End Tournament: " + selectedTournament.getName());
        confirmAlert.setContentText("Are you absolutely sure you want to end this tournament?\n\n" +
                                   "This action is PERMANENT and CANNOT be undone.\n\n" +
                                   "The tournament and all its matches will be DELETED from the database.\n\n" +
                                   "Tournament: " + selectedTournament.getName() + "\n" +
                                   "Teams: " + selectedTournament.getTeams().size() + "\n" +
                                   "Matches: " + selectedTournament.getMatches().size());
        
        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);
        
        if (result != ButtonType.OK) {
            statusLabel.setText("Tournament end cancelled.");
            statusLabel.setStyle("-fx-text-fill: #666;");
            return;
        }
        
        try {
            // Delete the tournament and its matches
            boolean success = tournamentService.deleteTournament(selectedTournament.getId());
            
            if (success) {
                statusLabel.setText("Tournament deleted successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                String finalNotes = finalNotesArea.getText().trim();
                String notesMessage = finalNotes.isEmpty() ? "" : "\n\nFinal Notes: " + finalNotes;
                
                showAlert("Success", 
                         "Tournament '" + selectedTournament.getName() + "' has been ended and deleted!\n\n" +
                         "The tournament and all its matches have been removed from the database.\n" +
                         "Total Teams: " + selectedTournament.getTeams().size() + "\n" +
                         "Total Matches Deleted: " + selectedTournament.getMatches().size() + "\n" +
                         "Completed Matches: " + completedMatchesLabel.getText() +
                         notesMessage,
                         Alert.AlertType.INFORMATION);
                
                // Close dialog after success
                Stage stage = (Stage) tournamentComboBox.getScene().getWindow();
                stage.close();
                
            } else {
                statusLabel.setText("Failed to delete tournament");
                statusLabel.setStyle("-fx-text-fill: red;");
                showAlert("Error", "Failed to delete tournament. Please try again.", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle cancel button click.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) tournamentComboBox.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Show an alert dialog.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
