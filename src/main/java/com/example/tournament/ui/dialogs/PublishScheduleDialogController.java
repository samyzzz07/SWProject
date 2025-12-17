package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controller for the Publish Schedule Dialog.
 */
public class PublishScheduleDialogController {
    
    @FXML
    private ComboBox<String> tournamentComboBox;
    
    @FXML
    private TableView<MatchData> scheduleTableView;
    
    @FXML
    private TableColumn<MatchData, String> matchColumn;
    
    @FXML
    private TableColumn<MatchData, String> dateTimeColumn;
    
    @FXML
    private TableColumn<MatchData, String> venueColumn;
    
    @FXML
    private TableColumn<MatchData, String> statusColumn;
    
    @FXML
    private Label totalMatchesLabel;
    
    @FXML
    private Label startDateLabel;
    
    @FXML
    private Label endDateLabel;
    
    @FXML
    private Label publishedStatusLabel;
    
    @FXML
    private CheckBox notifyTeamsCheckBox;
    
    @FXML
    private CheckBox publicAccessCheckBox;
    
    private boolean isPublished = false;
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Populate tournaments
        tournamentComboBox.setItems(FXCollections.observableArrayList(
            "Winter Championship 2025",
            "Spring League 2025",
            "Summer Cup 2025"
        ));
        
        // Set up table columns
        matchColumn.setCellValueFactory(new PropertyValueFactory<>("match"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Set default selection
        tournamentComboBox.setValue("Winter Championship 2025");
        handleTournamentSelection();
    }
    
    /**
     * Handle tournament selection change.
     */
    @FXML
    private void handleTournamentSelection() {
        String selected = tournamentComboBox.getValue();
        if (selected != null) {
            // Create sample schedule data
            ObservableList<MatchData> scheduleData = FXCollections.observableArrayList(
                new MatchData("Team Alpha vs Team Beta", "Dec 20, 2025 - 14:00", "Stadium A", "Scheduled"),
                new MatchData("Team Gamma vs Team Delta", "Dec 21, 2025 - 16:00", "Stadium B", "Scheduled"),
                new MatchData("Team Epsilon vs Team Zeta", "Dec 22, 2025 - 18:00", "Stadium A", "Scheduled"),
                new MatchData("Team Alpha vs Team Gamma", "Dec 23, 2025 - 14:00", "Stadium C", "Scheduled"),
                new MatchData("Team Beta vs Team Delta", "Dec 24, 2025 - 16:00", "Stadium B", "Scheduled")
            );
            
            scheduleTableView.setItems(scheduleData);
            
            // Update schedule info
            totalMatchesLabel.setText(String.valueOf(scheduleData.size()));
            startDateLabel.setText("December 20, 2025");
            endDateLabel.setText("December 24, 2025");
            publishedStatusLabel.setText(isPublished ? "Published" : "Not Published");
        }
    }
    
    /**
     * Handle publish button click.
     */
    @FXML
    private void handlePublish() {
        String tournament = tournamentComboBox.getValue();
        
        if (tournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament to publish.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm publication
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Publication");
        confirm.setHeaderText("Publish Schedule for " + tournament + "?");
        confirm.setContentText("This will make the schedule available to all teams and participants.\n\n" +
                              "Notify teams: " + (notifyTeamsCheckBox.isSelected() ? "Yes" : "No") + "\n" +
                              "Public access: " + (publicAccessCheckBox.isSelected() ? "Yes" : "No"));
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                isPublished = true;
                publishedStatusLabel.setText("Published");
                publishedStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                showAlert("Success", "Schedule has been published successfully!\n\n" +
                         "Tournament: " + tournament + "\n" +
                         "Total Matches: " + totalMatchesLabel.getText() + "\n" +
                         (notifyTeamsCheckBox.isSelected() ? "Teams have been notified via email.\n" : "") +
                         (publicAccessCheckBox.isSelected() ? "Schedule is now publicly accessible." : ""),
                         Alert.AlertType.INFORMATION);
            }
        });
    }
    
    /**
     * Handle preview button click.
     */
    @FXML
    private void handlePreview() {
        showAlert("Schedule Preview", "Opening schedule preview in new window...", Alert.AlertType.INFORMATION);
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
    
    /**
     * Data class for match information.
     */
    public static class MatchData {
        private final SimpleStringProperty match;
        private final SimpleStringProperty dateTime;
        private final SimpleStringProperty venue;
        private final SimpleStringProperty status;
        
        public MatchData(String match, String dateTime, String venue, String status) {
            this.match = new SimpleStringProperty(match);
            this.dateTime = new SimpleStringProperty(dateTime);
            this.venue = new SimpleStringProperty(venue);
            this.status = new SimpleStringProperty(status);
        }
        
        public String getMatch() { return match.get(); }
        public String getDateTime() { return dateTime.get(); }
        public String getVenue() { return venue.get(); }
        public String getStatus() { return status.get(); }
    }
}
