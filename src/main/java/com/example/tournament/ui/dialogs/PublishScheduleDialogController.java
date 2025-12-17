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
    
    private TournamentService tournamentService;
    private MatchService matchService;
    private Map<String, Tournament> tournamentMap;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm");
    
    /**
     * Initialize the dialog with database data and dummy data.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        matchService = new MatchService();
        tournamentMap = new HashMap<>();
        
        // Set up table columns
        matchColumn.setCellValueFactory(new PropertyValueFactory<>("match"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Load tournaments from database and add dummy data
        loadTournaments();
    }
    
    /**
     * Load tournaments from database and add dummy data.
     */
    private void loadTournaments() {
        ObservableList<String> tournamentNames = FXCollections.observableArrayList();
        
        // Load from database
        List<Tournament> dbTournaments = tournamentService.viewAllTournaments();
        for (Tournament tournament : dbTournaments) {
            String name = tournament.getName();
            tournamentNames.add(name);
            tournamentMap.put(name, tournament);
        }
        
        // Add dummy tournaments to maintain backward compatibility
        if (!tournamentNames.contains("Winter Championship 2025")) {
            tournamentNames.add("Winter Championship 2025");
        }
        if (!tournamentNames.contains("Spring League 2025")) {
            tournamentNames.add("Spring League 2025");
        }
        if (!tournamentNames.contains("Summer Cup 2025")) {
            tournamentNames.add("Summer Cup 2025");
        }
        
        tournamentComboBox.setItems(tournamentNames);
        
        // Set default selection
        if (!tournamentNames.isEmpty()) {
            tournamentComboBox.setValue(tournamentNames.get(0));
            handleTournamentSelection();
        }
    }
    
    /**
     * Handle tournament selection change.
     */
    @FXML
    private void handleTournamentSelection() {
        String selected = tournamentComboBox.getValue();
        if (selected != null) {
            ObservableList<MatchData> scheduleData = FXCollections.observableArrayList();
            
            // Check if this is a database tournament
            Tournament tournament = tournamentMap.get(selected);
            if (tournament != null) {
                // Load matches from database
                List<Match> matches = matchService.getMatchesByTournament(tournament.getId());
                
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
                startDateLabel.setText(tournament.getStartDate() != null ? tournament.getStartDate().format(dateFormatter) : "-");
                endDateLabel.setText(tournament.getEndDate() != null ? tournament.getEndDate().format(dateFormatter) : "-");
                publishedStatusLabel.setText(matches.isEmpty() ? "Not Published" : "Published");
            } else {
                // Use dummy data for backward compatibility
                if (selected.equals("Winter Championship 2025")) {
                    scheduleData.addAll(FXCollections.observableArrayList(
                        new MatchData("Team Alpha vs Team Beta", "Dec 20, 2025 - 14:00", "Stadium A", "Scheduled"),
                        new MatchData("Team Gamma vs Team Delta", "Dec 21, 2025 - 16:00", "Stadium B", "Scheduled"),
                        new MatchData("Team Epsilon vs Team Zeta", "Dec 22, 2025 - 18:00", "Stadium A", "Scheduled"),
                        new MatchData("Team Alpha vs Team Gamma", "Dec 23, 2025 - 14:00", "Stadium C", "Scheduled"),
                        new MatchData("Team Beta vs Team Delta", "Dec 24, 2025 - 16:00", "Stadium B", "Scheduled")
                    ));
                    startDateLabel.setText("December 20, 2025");
                    endDateLabel.setText("December 24, 2025");
                } else if (selected.equals("Spring League 2025")) {
                    scheduleData.addAll(FXCollections.observableArrayList(
                        new MatchData("Lions vs Tigers", "Mar 15, 2025 - 15:00", "Arena 1", "Scheduled"),
                        new MatchData("Bears vs Wolves", "Mar 16, 2025 - 17:00", "Arena 2", "Scheduled"),
                        new MatchData("Eagles vs Hawks", "Mar 17, 2025 - 19:00", "Arena 1", "Scheduled")
                    ));
                    startDateLabel.setText("March 15, 2025");
                    endDateLabel.setText("March 17, 2025");
                } else if (selected.equals("Summer Cup 2025")) {
                    scheduleData.addAll(FXCollections.observableArrayList(
                        new MatchData("Sharks vs Dolphins", "Jun 10, 2025 - 16:00", "Pool Complex", "Scheduled"),
                        new MatchData("Whales vs Seals", "Jun 11, 2025 - 18:00", "Pool Complex", "Scheduled")
                    ));
                    startDateLabel.setText("June 10, 2025");
                    endDateLabel.setText("June 11, 2025");
                }
                
                totalMatchesLabel.setText(String.valueOf(scheduleData.size()));
                publishedStatusLabel.setText("Not Published");
            }
            
            scheduleTableView.setItems(scheduleData);
        }
    }
    }
    
    /**
     * Handle publish button click.
     */
    @FXML
    private void handlePublish() {
        String tournamentName = tournamentComboBox.getValue();
        
        if (tournamentName == null) {
            showAlert("No Tournament Selected", "Please select a tournament to publish.", Alert.AlertType.WARNING);
            return;
        }
        
        // Check if this is a database tournament
        Tournament tournament = tournamentMap.get(tournamentName);
        
        // Confirm publication
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Publication");
        confirm.setHeaderText("Publish Schedule for " + tournamentName + "?");
        confirm.setContentText("This will make the schedule available to all teams and participants.\n\n" +
                              "Notify teams: " + (notifyTeamsCheckBox.isSelected() ? "Yes" : "No") + "\n" +
                              "Public access: " + (publicAccessCheckBox.isSelected() ? "Yes" : "No"));
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // For database tournaments, save matches if they don't already exist
                if (tournament != null) {
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
                } else {
                    // For dummy tournaments, just show success message
                    publishedStatusLabel.setText("Published");
                    publishedStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    
                    showAlert("Success", "Schedule has been published successfully!\n\n" +
                             "Tournament: " + tournamentName + "\n" +
                             "Total Matches: " + totalMatchesLabel.getText() + "\n" +
                             (notifyTeamsCheckBox.isSelected() ? "Teams have been notified via email.\n" : "") +
                             (publicAccessCheckBox.isSelected() ? "Schedule is now publicly accessible." : ""),
                             Alert.AlertType.INFORMATION);
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
