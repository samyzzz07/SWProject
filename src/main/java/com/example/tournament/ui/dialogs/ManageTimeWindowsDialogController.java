package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Match;
import com.example.tournament.model.TimeSlot;
import com.example.tournament.model.Tournament;
import com.example.tournament.model.Venue;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the Manage Time Windows Dialog.
 * Allows Game Coordinator to manage time slots and schedules for tournaments.
 */
public class ManageTimeWindowsDialogController {
    
    @FXML private ComboBox<Tournament> tournamentComboBox;
    @FXML private ComboBox<Match> matchComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<Venue> venueComboBox;
    @FXML private ListView<TimeSlot> timeSlotListView;
    @FXML private Label statusLabel;
    
    private TournamentService tournamentService;
    private ObservableList<Tournament> tournaments;
    private ObservableList<Match> matches;
    private ObservableList<TimeSlot> timeSlots;
    private ObservableList<Venue> venues;
    
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        
        // Load tournaments
        loadTournaments();
        
        // Load venues
        loadVenues();
        
        // Load time slots
        loadTimeSlots();
        
        // Set up tournament selection listener
        tournamentComboBox.setOnAction(e -> handleTournamentSelection());
        
        statusLabel.setText("Ready");
    }
    
    private void loadTournaments() {
        tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentComboBox.setItems(tournaments);
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
        } finally {
            em.close();
        }
    }
    
    private void loadTimeSlots() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<TimeSlot> query = em.createQuery(
                "SELECT ts FROM TimeSlot ts ORDER BY ts.startTime DESC",
                TimeSlot.class
            );
            timeSlots = FXCollections.observableArrayList(query.getResultList());
            timeSlotListView.setItems(timeSlots);
            
            // Custom cell factory to display time slots nicely
            timeSlotListView.setCellFactory(lv -> new ListCell<TimeSlot>() {
                @Override
                protected void updateItem(TimeSlot item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String venueInfo = item.getVenue() != null ? " at " + item.getVenue().getName() : "";
                        setText(item.getStartTime().format(formatter) + " - " + 
                               item.getEndTime().format(formatter) + venueInfo);
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
                            setText(team1 + " vs " + team2);
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
                            setText(team1 + " vs " + team2);
                        }
                    }
                });
            } finally {
                em.close();
            }
        }
    }
    
    @FXML
    private void handleAssignTimeWindow() {
        Match selectedMatch = matchComboBox.getValue();
        Venue selectedVenue = venueComboBox.getValue();
        
        if (selectedMatch == null) {
            showAlert("No Match Selected", "Please select a match to assign a time window.", Alert.AlertType.WARNING);
            return;
        }
        
        if (datePicker.getValue() == null || startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()) {
            showAlert("Incomplete Time Information", "Please provide date, start time, and end time.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Parse time inputs
            String[] startParts = startTimeField.getText().split(":");
            String[] endParts = endTimeField.getText().split(":");
            
            LocalDateTime startDateTime = datePicker.getValue()
                .atTime(Integer.parseInt(startParts[0]), Integer.parseInt(startParts[1]));
            LocalDateTime endDateTime = datePicker.getValue()
                .atTime(Integer.parseInt(endParts[0]), Integer.parseInt(endParts[1]));
            
            // Create and save time slot
            EntityManager em = JPAUtil.getEntityManager();
            try {
                em.getTransaction().begin();
                
                TimeSlot timeSlot = new TimeSlot(startDateTime, endDateTime);
                if (selectedVenue != null) {
                    timeSlot.setVenue(selectedVenue);
                }
                em.persist(timeSlot);
                
                // Update match with time slot
                Match match = em.find(Match.class, selectedMatch.getId());
                match.setTimeSlot(timeSlot);
                match.setScheduledTime(startDateTime);
                // Venue is set on TimeSlot, Match will reference it through TimeSlot
                em.merge(match);
                
                em.getTransaction().commit();
                
                statusLabel.setText("Time window assigned successfully!");
                showAlert("Success", "Time window has been assigned to the match.", Alert.AlertType.INFORMATION);
                
                // Refresh data
                loadTimeSlots();
                clearFields();
                
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                showAlert("Error", "Failed to assign time window: " + e.getMessage(), Alert.AlertType.ERROR);
            } finally {
                em.close();
            }
            
        } catch (Exception e) {
            showAlert("Invalid Time Format", "Please enter time in HH:MM format (e.g., 14:30)", Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadTournaments();
        loadVenues();
        loadTimeSlots();
        statusLabel.setText("Data refreshed");
    }
    
    @FXML
    private void handleClose() {
        Stage stage = (Stage) tournamentComboBox.getScene().getWindow();
        stage.close();
    }
    
    private void clearFields() {
        matchComboBox.setValue(null);
        datePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        venueComboBox.setValue(null);
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
