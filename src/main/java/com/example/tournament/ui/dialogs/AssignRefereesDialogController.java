package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the Assign Referees Dialog.
 */
public class AssignRefereesDialogController {
    
    @FXML
    private ComboBox<String> matchComboBox;
    
    @FXML
    private Label matchDetailsLabel;
    
    @FXML
    private ListView<String> refereeListView;
    
    @FXML
    private Label currentRefereeLabel;
    
    // Store match-referee assignments
    private Map<String, String> matchRefereeAssignments = new HashMap<>();
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Populate sample matches
        ObservableList<String> matches = FXCollections.observableArrayList(
            "Match 1: Team Alpha vs Team Beta - Dec 20, 2025 14:00",
            "Match 2: Team Gamma vs Team Delta - Dec 21, 2025 16:00",
            "Match 3: Team Epsilon vs Team Zeta - Dec 22, 2025 18:00",
            "Match 4: Team Alpha vs Team Gamma - Dec 23, 2025 14:00",
            "Match 5: Team Beta vs Team Delta - Dec 24, 2025 16:00"
        );
        matchComboBox.setItems(matches);
        
        // Populate sample referees
        ObservableList<String> referees = FXCollections.observableArrayList(
            "John Smith - Certified Level A",
            "Jane Doe - Certified Level A",
            "Mike Johnson - Certified Level B",
            "Sarah Williams - Certified Level A",
            "David Brown - Certified Level B",
            "Emily Davis - Certified Level A"
        );
        refereeListView.setItems(referees);
        
        // Set up match selection listener
        matchComboBox.setOnAction(event -> updateMatchDetails());
    }
    
    /**
     * Update match details when a match is selected.
     */
    private void updateMatchDetails() {
        String selectedMatch = matchComboBox.getValue();
        if (selectedMatch != null) {
            matchDetailsLabel.setText("Venue: Stadium A | Duration: 90 minutes");
            
            // Check if referee is already assigned
            String assignedReferee = matchRefereeAssignments.get(selectedMatch);
            if (assignedReferee != null) {
                currentRefereeLabel.setText(assignedReferee);
                currentRefereeLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            } else {
                currentRefereeLabel.setText("None");
                currentRefereeLabel.setStyle("-fx-text-fill: #f44336;");
            }
        }
    }
    
    /**
     * Handle assign button click.
     */
    @FXML
    private void handleAssign() {
        String selectedMatch = matchComboBox.getValue();
        String selectedReferee = refereeListView.getSelectionModel().getSelectedItem();
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to assign a referee.", Alert.AlertType.WARNING);
            return;
        }
        
        if (selectedReferee == null) {
            showAlert("No Referee Selected", "Please select a referee to assign.", Alert.AlertType.WARNING);
            return;
        }
        
        // Assign referee to match
        matchRefereeAssignments.put(selectedMatch, selectedReferee);
        currentRefereeLabel.setText(selectedReferee);
        currentRefereeLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        
        showAlert("Success", "Referee " + selectedReferee + " has been assigned to " + selectedMatch, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle clear assignment button click.
     */
    @FXML
    private void handleClear() {
        String selectedMatch = matchComboBox.getValue();
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to clear the referee assignment.", Alert.AlertType.WARNING);
            return;
        }
        
        matchRefereeAssignments.remove(selectedMatch);
        currentRefereeLabel.setText("None");
        currentRefereeLabel.setStyle("-fx-text-fill: #f44336;");
        
        showAlert("Cleared", "Referee assignment has been cleared for " + selectedMatch, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) matchComboBox.getScene().getWindow();
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
