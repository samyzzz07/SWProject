package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for GameManager interface.
 * GameManager can perform: AddGames, CollectFees, ViewSchedule, ManageReferee, 
 * FinalTournament, PostScores, RecordMatchResults
 */
public class GameManagerController {
    
    @FXML private Label welcomeLabel;
    @FXML private ListView<Match> scheduleListView;
    @FXML private ListView<Referee> refereeListView;
    @FXML private ListView<Tournament> tournamentListView;
    @FXML private TextField team1ScoreField;
    @FXML private TextField team2ScoreField;
    @FXML private Button postScoreButton;
    @FXML private Button addGameButton;
    @FXML private Button finalizeButton;
    @FXML private Button manageRefereeButton;
    @FXML private Label statusLabel;
    
    private User currentUser;
    private ScheduleService scheduleService;
    private RefereeService refereeService;
    private TournamentService tournamentService;
    private ScoringService scoringService;
    private GameService gameService;
    
    @FXML
    public void initialize() {
        scheduleService = new ScheduleService();
        refereeService = new RefereeService();
        tournamentService = new TournamentService();
        scoringService = new ScoringService();
        gameService = new GameService();
        
        statusLabel.setText("Ready");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (Game Manager)");
        loadData();
    }
    
    private void loadData() {
        // ViewSchedule
        ObservableList<Match> schedule = FXCollections.observableArrayList(
            scheduleService.viewSchedule()
        );
        scheduleListView.setItems(schedule);
        
        // View Referees
        ObservableList<Referee> referees = FXCollections.observableArrayList(
            refereeService.getAllReferees()
        );
        refereeListView.setItems(referees);
        
        // View Tournaments
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList(
            tournamentService.viewAllTournaments()
        );
        tournamentListView.setItems(tournaments);
    }
    
    @FXML
    private void handlePostScore() {
        Match selected = scheduleListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a match first");
            return;
        }
        
        try {
            int team1Score = Integer.parseInt(team1ScoreField.getText());
            int team2Score = Integer.parseInt(team2ScoreField.getText());
            
            if (scoringService.postScores(selected.getId(), team1Score, team2Score)) {
                statusLabel.setText("Scores posted successfully!");
                team1ScoreField.clear();
                team2ScoreField.clear();
                loadData(); // Refresh
            } else {
                statusLabel.setText("Failed to post scores");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid score values");
        }
    }
    
    @FXML
    private void handleAddGame() {
        statusLabel.setText("Add game functionality");
        showAlert("Add Game", "This would open a dialog to add a new game");
    }
    
    @FXML
    private void handleFinalizeTournament() {
        Tournament selected = tournamentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a tournament first");
            return;
        }
        
        if (tournamentService.finalizeTournament(selected.getId())) {
            statusLabel.setText("Tournament finalized successfully!");
            loadData(); // Refresh
        } else {
            statusLabel.setText("Failed to finalize tournament");
        }
    }
    
    @FXML
    private void handleManageReferee() {
        statusLabel.setText("Manage referee functionality");
        showAlert("Manage Referee", "This would open a dialog to add/edit/remove referees");
    }
    
    @FXML
    private void handleCollectFees() {
        statusLabel.setText("Fee collection functionality");
        showAlert("Collect Fees", "This would open a dialog for fee collection");
    }
    
    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_profile_view.fxml"));
            Parent root = loader.load();
            
            UserProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.setTitle("User Profile");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load profile view: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") || title.toLowerCase().contains("fail") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
