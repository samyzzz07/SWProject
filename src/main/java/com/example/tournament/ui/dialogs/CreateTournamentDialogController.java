package com.example.tournament.ui.dialogs;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import com.example.tournament.service.SportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import java.time.LocalDate;

/**
 * Controller for the Create Tournament Dialog.
 */
public class CreateTournamentDialogController {
    
    @FXML
    private TextField tournamentNameField;
    
    @FXML
    private ComboBox<String> sportComboBox;
    
    @FXML
    private ComboBox<String> tournamentTypeComboBox;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private Spinner<Integer> maxTeamsSpinner;
    
    @FXML
    private DatePicker registrationDeadlinePicker;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Label statusLabel;
    
    private TournamentService tournamentService;
    private SportService sportService;
    private boolean tournamentCreated = false;
    
    /**
     * Initialize the dialog.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        sportService = new SportService();
        
        // Populate sport combo box
        sportComboBox.setItems(FXCollections.observableArrayList(
            "Football", "Basketball", "Cricket", "Tennis", "Volleyball", "Baseball"
        ));
        
        // Populate tournament type combo box
        tournamentTypeComboBox.setItems(FXCollections.observableArrayList(
            "League", "Knockout", "Round Robin"
        ));
        
        // Configure max teams spinner
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 64, 16, 2);
        maxTeamsSpinner.setValueFactory(valueFactory);
        maxTeamsSpinner.setEditable(true);
        
        // Set default dates
        startDatePicker.setValue(LocalDate.now().plusDays(7));
        endDatePicker.setValue(LocalDate.now().plusDays(37));
        registrationDeadlinePicker.setValue(LocalDate.now().plusDays(5));
    }
    
    /**
     * Handle create tournament button click.
     */
    @FXML
    private void handleCreate() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        try {
            String name = tournamentNameField.getText().trim();
            String sportName = sportComboBox.getValue();
            String type = tournamentTypeComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String description = descriptionArea.getText().trim();
            
            // Get or create sport
            Sport sport = sportService.getSportByName(sportName);
            if (sport == null) {
                sport = new Sport(sportName);
                sport = sportService.createSport(sport);
            }
            
            // Create tournament based on type
            Tournament tournament;
            if ("Knockout".equalsIgnoreCase(type)) {
                tournament = new KnockoutTournament(name, sport, startDate, endDate);
            } else if ("Round Robin".equalsIgnoreCase(type)) {
                tournament = new RoundRobinTournament(name, sport, startDate, endDate);
            } else {
                // League type uses LeagueTournament
                tournament = new LeagueTournament(name, sport, startDate, endDate);
            }
            
            // Save to database
            boolean success = tournamentService.createTournament(tournament);
            
            if (success) {
                tournamentCreated = true;
                statusLabel.setText("Tournament created successfully!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                showAlert("Success", 
                         "Tournament '" + name + "' has been created successfully!\n\n" +
                         "Type: " + type + "\n" +
                         "Sport: " + sportName + "\n" +
                         "Start Date: " + startDate + "\n" +
                         "End Date: " + endDate,
                         Alert.AlertType.INFORMATION);
                
                // Close dialog after a brief delay
                Stage stage = (Stage) tournamentNameField.getScene().getWindow();
                stage.close();
            } else {
                statusLabel.setText("Failed to create tournament. Please try again.");
                statusLabel.setStyle("-fx-text-fill: red;");
                showAlert("Error", "Failed to create tournament. Please try again.", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Validate form input.
     */
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        
        if (tournamentNameField.getText() == null || tournamentNameField.getText().trim().isEmpty()) {
            errors.append("- Tournament name is required\n");
        }
        
        if (sportComboBox.getValue() == null) {
            errors.append("- Sport selection is required\n");
        }
        
        if (tournamentTypeComboBox.getValue() == null) {
            errors.append("- Tournament type is required\n");
        }
        
        if (startDatePicker.getValue() == null) {
            errors.append("- Start date is required\n");
        }
        
        if (endDatePicker.getValue() == null) {
            errors.append("- End date is required\n");
        }
        
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                errors.append("- End date must be after start date\n");
            }
        }
        
        if (registrationDeadlinePicker.getValue() != null && startDatePicker.getValue() != null) {
            if (registrationDeadlinePicker.getValue().isAfter(startDatePicker.getValue())) {
                errors.append("- Registration deadline must be before start date\n");
            }
        }
        
        if (errors.length() > 0) {
            statusLabel.setText("Please fix the following errors:");
            statusLabel.setStyle("-fx-text-fill: red;");
            showAlert("Validation Error", "Please fix the following errors:\n\n" + errors.toString(), 
                     Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    /**
     * Handle cancel button click.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) tournamentNameField.getScene().getWindow();
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
    
    /**
     * Check if a tournament was successfully created.
     */
    public boolean isTournamentCreated() {
        return tournamentCreated;
    }
}
