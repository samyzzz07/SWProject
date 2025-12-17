package com.example.tournament.ui.dialogs;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Controller for the Define Tournament Rules Dialog.
 */
public class DefineTournamentRulesDialogController {
    
    @FXML
    private ComboBox<String> tournamentComboBox;
    
    @FXML
    private Label tournamentNameLabel;
    
    @FXML
    private Label tournamentTypeLabel;
    
    @FXML
    private Label sportLabel;
    
    @FXML
    private Spinner<Integer> playersPerTeamSpinner;
    
    @FXML
    private Spinner<Integer> substitutesSpinner;
    
    @FXML
    private Spinner<Integer> matchDurationSpinner;
    
    @FXML
    private VBox leagueRulesBox;
    
    @FXML
    private Spinner<Integer> pointsForWinSpinner;
    
    @FXML
    private Spinner<Integer> pointsForDrawSpinner;
    
    @FXML
    private Spinner<Integer> pointsForLossSpinner;
    
    @FXML
    private VBox knockoutRulesBox;
    
    @FXML
    private Spinner<Integer> numberOfRoundsSpinner;
    
    @FXML
    private Spinner<Integer> extraTimeSpinner;
    
    @FXML
    private CheckBox penaltyShootoutCheckBox;
    
    @FXML
    private TextArea additionalRulesArea;
    
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
        
        // Configure spinners
        playersPerTeamSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 11));
        substitutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 3));
        matchDurationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 180, 90));
        
        pointsForWinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 3));
        pointsForDrawSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
        pointsForLossSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        
        numberOfRoundsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4));
        extraTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 30));
        
        // Make spinners editable
        playersPerTeamSpinner.setEditable(true);
        substitutesSpinner.setEditable(true);
        matchDurationSpinner.setEditable(true);
        pointsForWinSpinner.setEditable(true);
        pointsForDrawSpinner.setEditable(true);
        pointsForLossSpinner.setEditable(true);
        numberOfRoundsSpinner.setEditable(true);
        extraTimeSpinner.setEditable(true);
        
        // Default: hide both rule boxes
        leagueRulesBox.setVisible(false);
        leagueRulesBox.setManaged(false);
        knockoutRulesBox.setVisible(false);
        knockoutRulesBox.setManaged(false);
        
        // Load tournaments from database
        loadTournaments();
    }
    
    /**
     * Load tournaments from the database.
     */
    private void loadTournaments() {
        try {
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            ObservableList<String> tournamentNames = FXCollections.observableArrayList();
            
            for (Tournament tournament : tournaments) {
                tournamentNames.add(tournament.getName());
            }
            
            tournamentComboBox.setItems(tournamentNames);
            
            if (!tournamentNames.isEmpty()) {
                statusLabel.setText("Found " + tournamentNames.size() + " tournament(s)");
            } else {
                statusLabel.setText("No tournaments found. Please create a tournament first.");
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
                // Update tournament info
                tournamentNameLabel.setText(selectedTournament.getName());
                sportLabel.setText(selectedTournament.getSport() != null ? 
                    selectedTournament.getSport().getName() : "N/A");
                
                // Show appropriate rules based on tournament type
                if (selectedTournament instanceof LeagueTournament) {
                    tournamentTypeLabel.setText("League/Round Robin");
                    leagueRulesBox.setVisible(true);
                    leagueRulesBox.setManaged(true);
                    knockoutRulesBox.setVisible(false);
                    knockoutRulesBox.setManaged(false);
                    
                    // Load existing league rules
                    LeagueTournament league = (LeagueTournament) selectedTournament;
                    if (league.getPointsForWin() != null) {
                        pointsForWinSpinner.getValueFactory().setValue(league.getPointsForWin());
                    }
                    if (league.getPointsForDraw() != null) {
                        pointsForDrawSpinner.getValueFactory().setValue(league.getPointsForDraw());
                    }
                    if (league.getPointsForLoss() != null) {
                        pointsForLossSpinner.getValueFactory().setValue(league.getPointsForLoss());
                    }
                    
                } else if (selectedTournament instanceof RoundRobinTournament) {
                    tournamentTypeLabel.setText("Round Robin");
                    leagueRulesBox.setVisible(true);
                    leagueRulesBox.setManaged(true);
                    knockoutRulesBox.setVisible(false);
                    knockoutRulesBox.setManaged(false);
                    
                    // Load existing round robin rules
                    RoundRobinTournament roundRobin = (RoundRobinTournament) selectedTournament;
                    if (roundRobin.getPointsForWin() != null) {
                        pointsForWinSpinner.getValueFactory().setValue(roundRobin.getPointsForWin());
                    }
                    if (roundRobin.getPointsForDraw() != null) {
                        pointsForDrawSpinner.getValueFactory().setValue(roundRobin.getPointsForDraw());
                    }
                    if (roundRobin.getPointsForLoss() != null) {
                        pointsForLossSpinner.getValueFactory().setValue(roundRobin.getPointsForLoss());
                    }
                    
                } else if (selectedTournament instanceof KnockoutTournament) {
                    tournamentTypeLabel.setText("Knockout");
                    leagueRulesBox.setVisible(false);
                    leagueRulesBox.setManaged(false);
                    knockoutRulesBox.setVisible(true);
                    knockoutRulesBox.setManaged(true);
                    
                    // Load existing knockout rules
                    KnockoutTournament knockout = (KnockoutTournament) selectedTournament;
                    if (knockout.getCurrentRound() != null) {
                        numberOfRoundsSpinner.getValueFactory().setValue(knockout.getCurrentRound());
                    }
                } else {
                    tournamentTypeLabel.setText("Unknown");
                    leagueRulesBox.setVisible(false);
                    leagueRulesBox.setManaged(false);
                    knockoutRulesBox.setVisible(false);
                    knockoutRulesBox.setManaged(false);
                }
                
                statusLabel.setText("Tournament selected. Configure rules below.");
                statusLabel.setStyle("-fx-text-fill: #666;");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading tournament details");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    /**
     * Handle save rules button click.
     */
    @FXML
    private void handleSaveRules() {
        if (selectedTournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament first.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Update tournament rules based on type
            if (selectedTournament instanceof LeagueTournament) {
                LeagueTournament league = (LeagueTournament) selectedTournament;
                league.setPointsForWin(pointsForWinSpinner.getValue());
                league.setPointsForDraw(pointsForDrawSpinner.getValue());
                league.setPointsForLoss(pointsForLossSpinner.getValue());
            } else if (selectedTournament instanceof RoundRobinTournament) {
                RoundRobinTournament roundRobin = (RoundRobinTournament) selectedTournament;
                roundRobin.setPointsForWin(pointsForWinSpinner.getValue());
                roundRobin.setPointsForDraw(pointsForDrawSpinner.getValue());
                roundRobin.setPointsForLoss(pointsForLossSpinner.getValue());
            } else if (selectedTournament instanceof KnockoutTournament) {
                KnockoutTournament knockout = (KnockoutTournament) selectedTournament;
                knockout.setCurrentRound(numberOfRoundsSpinner.getValue());
            }
            
            // Update tournament in database
            boolean success = tournamentService.updateTournament(selectedTournament);
            
            if (success) {
                statusLabel.setText("Rules saved successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                StringBuilder rulesMessage = new StringBuilder();
                rulesMessage.append("Tournament: ").append(selectedTournament.getName()).append("\n\n");
                rulesMessage.append("General Rules:\n");
                rulesMessage.append("- Players per Team: ").append(playersPerTeamSpinner.getValue()).append("\n");
                rulesMessage.append("- Substitutes: ").append(substitutesSpinner.getValue()).append("\n");
                rulesMessage.append("- Match Duration: ").append(matchDurationSpinner.getValue()).append(" mins\n");
                
                if (selectedTournament instanceof LeagueTournament) {
                    rulesMessage.append("\nLeague Rules:\n");
                    rulesMessage.append("- Points for Win: ").append(pointsForWinSpinner.getValue()).append("\n");
                    rulesMessage.append("- Points for Draw: ").append(pointsForDrawSpinner.getValue()).append("\n");
                    rulesMessage.append("- Points for Loss: ").append(pointsForLossSpinner.getValue()).append("\n");
                } else if (selectedTournament instanceof RoundRobinTournament) {
                    rulesMessage.append("\nRound Robin Rules:\n");
                    rulesMessage.append("- Points for Win: ").append(pointsForWinSpinner.getValue()).append("\n");
                    rulesMessage.append("- Points for Draw: ").append(pointsForDrawSpinner.getValue()).append("\n");
                    rulesMessage.append("- Points for Loss: ").append(pointsForLossSpinner.getValue()).append("\n");
                } else if (selectedTournament instanceof KnockoutTournament) {
                    rulesMessage.append("\nKnockout Rules:\n");
                    rulesMessage.append("- Number of Rounds: ").append(numberOfRoundsSpinner.getValue()).append("\n");
                    rulesMessage.append("- Extra Time: ").append(extraTimeSpinner.getValue()).append(" mins\n");
                    rulesMessage.append("- Penalty Shootout: ").append(penaltyShootoutCheckBox.isSelected() ? "Yes" : "No").append("\n");
                }
                
                showAlert("Success", "Tournament rules have been saved!\n\n" + rulesMessage.toString(), 
                         Alert.AlertType.INFORMATION);
            } else {
                statusLabel.setText("Failed to save rules");
                statusLabel.setStyle("-fx-text-fill: red;");
                showAlert("Error", "Failed to save tournament rules. Please try again.", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
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
