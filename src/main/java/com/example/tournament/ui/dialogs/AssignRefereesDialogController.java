package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the Assign Referees Dialog.
 */
public class AssignRefereesDialogController {
    
    @FXML
    private ComboBox<Tournament> tournamentComboBox;
    
    @FXML
    private ComboBox<Match> matchComboBox;
    
    @FXML
    private Label matchDetailsLabel;
    
    @FXML
    private ListView<String> refereeListView;
    
    @FXML
    private Label currentRefereeLabel;
    
    private TournamentService tournamentService;
    private ObservableList<Tournament> tournaments;
    private ObservableList<Match> matches;
    
    // Store match-referee assignments
    private Map<Long, String> matchRefereeAssignments = new HashMap<>();
    
    /**
     * Initialize the dialog with data from database.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        
        // Load tournaments from database
        loadTournaments();
        
        // Populate sample referees (can be loaded from database in future)
        ObservableList<String> referees = FXCollections.observableArrayList(
            "John Smith - Certified Level A",
            "Jane Doe - Certified Level A",
            "Mike Johnson - Certified Level B",
            "Sarah Williams - Certified Level A",
            "David Brown - Certified Level B",
            "Emily Davis - Certified Level A"
        );
        refereeListView.setItems(referees);
        
        // Set up tournament selection listener
        tournamentComboBox.setOnAction(event -> handleTournamentSelection());
        
        // Set up match selection listener
        matchComboBox.setOnAction(event -> updateMatchDetails());
    }
    
    private void loadTournaments() {
        tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentComboBox.setItems(tournaments);
        
        // Custom cell factory for tournaments to show user-friendly names
        tournamentComboBox.setCellFactory(lv -> new ListCell<Tournament>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String sportName = item.getSport() != null ? " (" + item.getSport().getName() + ")" : "";
                    setText(item.getName() + sportName);
                }
            }
        });
        tournamentComboBox.setButtonCell(new ListCell<Tournament>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String sportName = item.getSport() != null ? " (" + item.getSport().getName() + ")" : "";
                    setText(item.getName() + sportName);
                }
            }
        });
    }
    
    private void handleTournamentSelection() {
        Tournament selected = tournamentComboBox.getValue();
        if (selected != null) {
            EntityManager em = JPAUtil.getEntityManager();
            try {
                // Load matches for selected tournament
                TypedQuery<Match> query = em.createQuery(
                    "SELECT m FROM Match m WHERE m.tournament = :tournament ORDER BY m.id",
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
                                " - " + item.getScheduledTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) : "";
                            setText(team1 + " vs " + team2 + time);
                        }
                    }
                });
                matchComboBox.setButtonCell(new ListCell<Match>() {
                    @Override
                    protected void updateItem(Match item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            String team1 = item.getTeam1() != null ? item.getTeam1().getName() : "TBD";
                            String team2 = item.getTeam2() != null ? item.getTeam2().getName() : "TBD";
                            String time = item.getScheduledTime() != null ? 
                                " - " + item.getScheduledTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) : "";
                            setText(team1 + " vs " + team2 + time);
                        }
                    }
                });
                
                // Clear previous selection
                clearMatchDetails();
            } finally {
                em.close();
            }
        }
    }
    
    /**
     * Update match details when a match is selected.
     */
    private void updateMatchDetails() {
        Match selectedMatch = matchComboBox.getValue();
        if (selectedMatch != null) {
            // Display match details
            String details = "";
            if (selectedMatch.getVenue() != null) {
                details = "Venue: " + selectedMatch.getVenue().getName();
            } else {
                details = "Venue: Not assigned";
            }
            if (selectedMatch.getScheduledTime() != null) {
                details += " | Time: " + selectedMatch.getScheduledTime().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            matchDetailsLabel.setText(details);
            
            // Check if referee is already assigned
            String assignedReferee = matchRefereeAssignments.get(selectedMatch.getId());
            if (assignedReferee != null) {
                currentRefereeLabel.setText(assignedReferee);
                currentRefereeLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            } else {
                currentRefereeLabel.setText("None");
                currentRefereeLabel.setStyle("-fx-text-fill: #f44336;");
            }
        }
    }
    
    private void clearMatchDetails() {
        matchComboBox.setValue(null);
        matchDetailsLabel.setText("");
        currentRefereeLabel.setText("None");
        currentRefereeLabel.setStyle("-fx-text-fill: #f44336;");
    }
    
    /**
     * Handle assign button click.
     */
    @FXML
    private void handleAssign() {
        Tournament selectedTournament = tournamentComboBox.getValue();
        Match selectedMatch = matchComboBox.getValue();
        String selectedReferee = refereeListView.getSelectionModel().getSelectedItem();
        
        if (selectedTournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament first.", Alert.AlertType.WARNING);
            return;
        }
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to assign a referee.", Alert.AlertType.WARNING);
            return;
        }
        
        if (selectedReferee == null) {
            showAlert("No Referee Selected", "Please select a referee to assign.", Alert.AlertType.WARNING);
            return;
        }
        
        // Assign referee to match
        matchRefereeAssignments.put(selectedMatch.getId(), selectedReferee);
        currentRefereeLabel.setText(selectedReferee);
        currentRefereeLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        
        String team1 = selectedMatch.getTeam1() != null ? selectedMatch.getTeam1().getName() : "TBD";
        String team2 = selectedMatch.getTeam2() != null ? selectedMatch.getTeam2().getName() : "TBD";
        showAlert("Success", "Referee " + selectedReferee + " has been assigned to match: " + 
                 team1 + " vs " + team2, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle clear assignment button click.
     */
    @FXML
    private void handleClear() {
        Match selectedMatch = matchComboBox.getValue();
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to clear the referee assignment.", Alert.AlertType.WARNING);
            return;
        }
        
        matchRefereeAssignments.remove(selectedMatch.getId());
        currentRefereeLabel.setText("None");
        currentRefereeLabel.setStyle("-fx-text-fill: #f44336;");
        
        String team1 = selectedMatch.getTeam1() != null ? selectedMatch.getTeam1().getName() : "TBD";
        String team2 = selectedMatch.getTeam2() != null ? selectedMatch.getTeam2().getName() : "TBD";
        showAlert("Cleared", "Referee assignment has been cleared for match: " + 
                 team1 + " vs " + team2, Alert.AlertType.INFORMATION);
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
