package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.model.Venue;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the Postpone Match Dialog.
 */
public class PostponeMatchDialogController {
    
    @FXML
    private ComboBox<Tournament> tournamentComboBox;
    
    @FXML
    private ComboBox<Match> matchComboBox;
    
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
    private ComboBox<Venue> venueComboBox;
    
    @FXML
    private TextArea reasonArea;
    
    private TournamentService tournamentService;
    private ObservableList<Tournament> tournaments;
    private ObservableList<Match> matches;
    private ObservableList<Venue> venues;
    
    /**
     * Initialize the dialog with data from database.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        
        // Load tournaments from database
        loadTournaments();
        
        // Load venues from database
        loadVenues();
        
        // Set minimum date to today
        newDatePicker.setValue(LocalDate.now().plusDays(1));
        
        // Set up tournament selection listener
        tournamentComboBox.setOnAction(event -> handleTournamentSelection());
        
        // Set up match selection listener
        matchComboBox.setOnAction(event -> handleMatchSelection());
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
    
    private void loadVenues() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Venue> query = em.createQuery(
                "SELECT v FROM Venue v ORDER BY v.name",
                Venue.class
            );
            venues = FXCollections.observableArrayList(query.getResultList());
            venueComboBox.setItems(venues);
            
            // Custom cell factory for venues
            venueComboBox.setCellFactory(lv -> new ListCell<Venue>() {
                @Override
                protected void updateItem(Venue item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String location = item.getLocation() != null ? " - " + item.getLocation() : "";
                        setText(item.getName() + location);
                    }
                }
            });
            venueComboBox.setButtonCell(new ListCell<Venue>() {
                @Override
                protected void updateItem(Venue item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String location = item.getLocation() != null ? " - " + item.getLocation() : "";
                        setText(item.getName() + location);
                    }
                }
            });
        } finally {
            em.close();
        }
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
     * Handle match selection change.
     */
    @FXML
    private void handleMatchSelection() {
        Match selectedMatch = matchComboBox.getValue();
        if (selectedMatch != null) {
            // Display match information
            if (selectedMatch.getScheduledTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy - HH:mm");
                String dateTime = selectedMatch.getScheduledTime().format(formatter);
                matchDetailsLabel.setText("Scheduled for " + dateTime);
                currentDateLabel.setText(dateTime);
            } else {
                matchDetailsLabel.setText("Match time not yet scheduled");
                currentDateLabel.setText("Not scheduled");
            }
            
            if (selectedMatch.getVenue() != null) {
                String venueName = selectedMatch.getVenue().getName();
                String location = selectedMatch.getVenue().getLocation() != null ? 
                    " - " + selectedMatch.getVenue().getLocation() : "";
                currentVenueLabel.setText(venueName + location);
            } else {
                currentVenueLabel.setText("Not assigned");
            }
        }
    }
    
    private void clearMatchDetails() {
        matchComboBox.setValue(null);
        matchDetailsLabel.setText("");
        currentDateLabel.setText("-");
        currentVenueLabel.setText("-");
    }
    
    /**
     * Handle postpone button click.
     */
    @FXML
    private void handlePostpone() {
        Tournament selectedTournament = tournamentComboBox.getValue();
        Match selectedMatch = matchComboBox.getValue();
        LocalDate newDate = newDatePicker.getValue();
        String newTime = newTimeField.getText();
        Venue newVenue = venueComboBox.getValue();
        String reason = reasonArea.getText();
        
        // Validate inputs
        if (selectedTournament == null) {
            showAlert("No Tournament Selected", "Please select a tournament first.", Alert.AlertType.WARNING);
            return;
        }
        
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
        
        // Build match description
        String team1 = selectedMatch.getTeam1() != null ? selectedMatch.getTeam1().getName() : "TBD";
        String team2 = selectedMatch.getTeam2() != null ? selectedMatch.getTeam2().getName() : "TBD";
        String matchDesc = team1 + " vs " + team2;
        
        // Confirm postponement
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Postponement");
        confirm.setHeaderText("Postpone " + matchDesc + "?");
        confirm.setContentText("New date: " + newDate + " at " + newTime + "\n" +
                              "New venue: " + newVenue.getName() + "\n\n" +
                              "Both teams will be notified of this change.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showAlert("Success", matchDesc + " has been postponed!\n\n" +
                         "New schedule: " + newDate + " at " + newTime + "\n" +
                         "Venue: " + newVenue.getName() + "\n\n" +
                         "Reason: " + reason, Alert.AlertType.INFORMATION);
                
                // Clear form
                tournamentComboBox.setValue(null);
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
