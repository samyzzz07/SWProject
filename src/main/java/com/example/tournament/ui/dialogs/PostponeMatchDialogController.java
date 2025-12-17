package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

/**
 * Controller for the Postpone Match Dialog.
 */
public class PostponeMatchDialogController {
    
    @FXML
    private ComboBox<String> matchComboBox;
    
    @FXML
    private Label matchDetailsLabel;
    
    @FXML
    private Label currentDateLabel;
    
    @FXML
    private Label currentVenueLabel;
    
    @FXML
    private DatePicker newDatePicker;
    
    @FXML
    private TextField newTimeField;
    
    @FXML
    private ComboBox<String> venueComboBox;
    
    @FXML
    private TextArea reasonArea;
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Populate scheduled matches
        ObservableList<String> matches = FXCollections.observableArrayList(
            "Match 1: Team Alpha vs Team Beta",
            "Match 2: Team Gamma vs Team Delta",
            "Match 3: Team Epsilon vs Team Zeta",
            "Match 4: Team Alpha vs Team Gamma",
            "Match 5: Team Beta vs Team Delta"
        );
        matchComboBox.setItems(matches);
        
        // Populate venues
        ObservableList<String> venues = FXCollections.observableArrayList(
            "Stadium A - Main Field",
            "Stadium B - North Field",
            "Stadium C - South Field",
            "Arena D - Indoor Court"
        );
        venueComboBox.setItems(venues);
        
        // Set minimum date to today
        newDatePicker.setValue(LocalDate.now().plusDays(1));
    }
    
    /**
     * Handle match selection change.
     */
    @FXML
    private void handleMatchSelection() {
        String selectedMatch = matchComboBox.getValue();
        if (selectedMatch != null) {
            // Display sample match information
            matchDetailsLabel.setText("Scheduled for December 20, 2025 at 14:00");
            currentDateLabel.setText("December 20, 2025 - 14:00");
            currentVenueLabel.setText("Stadium A - Main Field");
        }
    }
    
    /**
     * Handle postpone button click.
     */
    @FXML
    private void handlePostpone() {
        String selectedMatch = matchComboBox.getValue();
        LocalDate newDate = newDatePicker.getValue();
        String newTime = newTimeField.getText();
        String newVenue = venueComboBox.getValue();
        String reason = reasonArea.getText();
        
        // Validate inputs
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to postpone.", Alert.AlertType.WARNING);
            return;
        }
        
        if (newDate == null) {
            showAlert("No Date Selected", "Please select a new date for the match.", Alert.AlertType.WARNING);
            return;
        }
        
        if (newTime == null || newTime.trim().isEmpty()) {
            showAlert("No Time Entered", "Please enter a time for the match.", Alert.AlertType.WARNING);
            return;
        }
        
        if (newVenue == null) {
            showAlert("No Venue Selected", "Please select a venue for the match.", Alert.AlertType.WARNING);
            return;
        }
        
        if (reason == null || reason.trim().isEmpty()) {
            showAlert("No Reason Provided", "Please provide a reason for postponing the match.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm postponement
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Postponement");
        confirm.setHeaderText("Postpone " + selectedMatch + "?");
        confirm.setContentText("New date: " + newDate + " at " + newTime + "\n" +
                              "New venue: " + newVenue + "\n\n" +
                              "Both teams will be notified of this change.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showAlert("Success", selectedMatch + " has been postponed!\n\n" +
                         "New schedule: " + newDate + " at " + newTime + "\n" +
                         "Venue: " + newVenue + "\n\n" +
                         "Reason: " + reason, Alert.AlertType.INFORMATION);
                
                // Clear form
                matchComboBox.setValue(null);
                matchDetailsLabel.setText("");
                currentDateLabel.setText("-");
                currentVenueLabel.setText("-");
                newDatePicker.setValue(LocalDate.now().plusDays(1));
                newTimeField.clear();
                venueComboBox.setValue(null);
                reasonArea.clear();
            }
        });
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
