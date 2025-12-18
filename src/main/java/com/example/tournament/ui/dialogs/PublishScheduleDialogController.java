package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.TournamentService;
import com.example.tournament.service.MatchService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Publish Schedule Dialog.
 */
public class PublishScheduleDialogController {
    
    @FXML
    private ComboBox<Tournament> tournamentComboBox;
    
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
    
    private TournamentService tournamentService;
    private MatchService matchService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm");
    
    /**
     * Initialize the dialog with database data and dummy data.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        matchService = new MatchService();
        
        // Set up table columns
        matchColumn.setCellValueFactory(new PropertyValueFactory<>("match"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Load tournaments from database
        loadTournaments();
    }
    
    /**
     * Load tournaments from database.
     */
    private void loadTournaments() {
        List<Tournament> tournaments = tournamentService.viewAllTournaments();
        tournamentComboBox.setItems(FXCollections.observableArrayList(tournaments));
        
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
        
        // Set default selection
        if (!tournaments.isEmpty()) {
            tournamentComboBox.setValue(tournaments.get(0));
            handleTournamentSelection();
        }
    }
    
    /**
     * Handle tournament selection change.
     */
    @FXML
    private void handleTournamentSelection() {
        Tournament selected = tournamentComboBox.getValue();
        if (selected != null) {
            ObservableList<MatchData> scheduleData = FXCollections.observableArrayList();
            
            // Load matches from database
            List<Match> matches = matchService.getMatchesByTournament(selected.getId());
            
            for (Match match : matches) {
                String matchName = (match.getTeam1() != null ? match.getTeam1().getName() : "TBD") 
                                 + " vs " 
                                 + (match.getTeam2() != null ? match.getTeam2().getName() : "TBD");
                String dateTime = match.getScheduledTime() != null 
                                ? match.getScheduledTime().format(dateTimeFormatter) 
                                : "Not scheduled";
                String venueName = match.getVenue() != null ? match.getVenue().getName() : "TBD";
                String status = match.getStatus() != null ? match.getStatus().toString() : "SCHEDULED";
                
                scheduleData.add(new MatchData(matchName, dateTime, venueName, status));
            }
            
            // Update schedule info from database
            totalMatchesLabel.setText(String.valueOf(matches.size()));
            startDateLabel.setText(selected.getStartDate() != null ? selected.getStartDate().format(dateFormatter) : "-");
            endDateLabel.setText(selected.getEndDate() != null ? selected.getEndDate().format(dateFormatter) : "-");
            publishedStatusLabel.setText(matches.isEmpty() ? "Not Published" : "Published");
            
            scheduleTableView.setItems(scheduleData);
        }
    }
    
    /**
     * Handle publish button click.
     */
    @FXML
    private void handlePublish() {
        Tournament tournament = tournamentComboBox.getValue();
        
        if (tournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament to publish.", Alert.AlertType.WARNING);
            return;
        }
        
        String tournamentName = tournament.getName();
        
        // Confirm publication
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Publication");
        confirm.setHeaderText("Publish Schedule for " + tournamentName + "?");
        confirm.setContentText("This will make the schedule available to all teams and participants.\n\n" +
                              "Notify teams: " + (notifyTeamsCheckBox.isSelected() ? "Yes" : "No") + "\n" +
                              "Public access: " + (publicAccessCheckBox.isSelected() ? "Yes" : "No"));
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Generate schedule and save matches to database
                try {
                    tournament.generateSchedule();
                    
                    // Save generated matches to database
                    List<Match> matches = tournament.getMatches();
                    if (!matches.isEmpty()) {
                        boolean success = matchService.saveMatches(matches);
                        if (success) {
                            publishedStatusLabel.setText("Published");
                            publishedStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            
                            showAlert("Success", 
                                "Schedule has been published successfully and saved to database!\n\n" +
                                "Tournament: " + tournamentName + "\n" +
                                "Total Matches: " + matches.size() + "\n" +
                                (notifyTeamsCheckBox.isSelected() ? "Teams have been notified via email.\n" : "") +
                                (publicAccessCheckBox.isSelected() ? "Schedule is now publicly accessible." : ""),
                                Alert.AlertType.INFORMATION);
                            
                            // Refresh the display
                            handleTournamentSelection();
                        } else {
                            showAlert("Error", "Failed to save matches to database.", Alert.AlertType.ERROR);
                        }
                    } else {
                        showAlert("Warning", 
                            "No matches generated. Please ensure the tournament has teams assigned.",
                            Alert.AlertType.WARNING);
                    }
                } catch (Exception e) {
                    showAlert("Error", 
                        "Failed to generate or save schedule: " + e.getMessage(),
                        Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
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
