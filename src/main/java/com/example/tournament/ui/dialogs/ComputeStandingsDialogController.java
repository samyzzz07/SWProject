package com.example.tournament.ui.dialogs;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import com.example.tournament.util.JPAUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.*;

/**
 * Controller for the Compute Standings Dialog.
 * Allows Game Coordinator to compute and view tournament standings.
 */
public class ComputeStandingsDialogController {
    
    @FXML private ComboBox<Tournament> tournamentComboBox;
    @FXML private TableView<StandingEntry> standingsTable;
    @FXML private TableColumn<StandingEntry, Integer> rankColumn;
    @FXML private TableColumn<StandingEntry, String> teamColumn;
    @FXML private TableColumn<StandingEntry, Integer> playedColumn;
    @FXML private TableColumn<StandingEntry, Integer> winsColumn;
    @FXML private TableColumn<StandingEntry, Integer> lossesColumn;
    @FXML private TableColumn<StandingEntry, Integer> drawsColumn;
    @FXML private TableColumn<StandingEntry, Integer> pointsColumn;
    @FXML private TableColumn<StandingEntry, String> goalsColumn;
    @FXML private Label tournamentInfoLabel;
    @FXML private Label statusLabel;
    
    private TournamentService tournamentService;
    private ObservableList<Tournament> tournaments;
    
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        
        // Set up table columns
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        playedColumn.setCellValueFactory(new PropertyValueFactory<>("played"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossesColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));
        drawsColumn.setCellValueFactory(new PropertyValueFactory<>("draws"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        goalsColumn.setCellValueFactory(new PropertyValueFactory<>("goals"));
        
        // Load tournaments
        loadTournaments();
        
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
    
    private void handleTournamentSelection() {
        Tournament selected = tournamentComboBox.getValue();
        if (selected != null) {
            // Display tournament info
            String info = "Tournament: " + selected.getName();
            if (selected.getSport() != null) {
                info += " (" + selected.getSport().getName() + ")";
            }
            info += "\nStatus: " + selected.getStatus();
            if (selected.getStartDate() != null && selected.getEndDate() != null) {
                info += "\nDuration: " + selected.getStartDate() + " to " + selected.getEndDate();
            }
            tournamentInfoLabel.setText(info);
            
            // Clear standings
            standingsTable.getItems().clear();
        }
    }
    
    @FXML
    private void handleComputeStandings() {
        Tournament selected = tournamentComboBox.getValue();
        
        if (selected == null) {
            showAlert("No Tournament Selected", "Please select a tournament to compute standings.", 
                     Alert.AlertType.WARNING);
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Fetch tournament with all matches
            Tournament tournament = tournamentService.getTournamentById(selected.getId());
            
            if (tournament == null) {
                showAlert("Error", "Tournament not found.", Alert.AlertType.ERROR);
                return;
            }
            
            // Compute standings
            Map<Long, StandingEntry> standingsMap = new HashMap<>();
            
            // Initialize standings for all teams
            for (Team team : tournament.getTeams()) {
                standingsMap.put(team.getId(), new StandingEntry(team.getName()));
            }
            
            // Process all completed matches
            List<Match> matches = tournament.getMatches();
            for (Match match : matches) {
                if (match.getStatus() == Match.MatchStatus.COMPLETED && 
                    match.getTeam1() != null && match.getTeam2() != null &&
                    match.getTeam1Score() != null && match.getTeam2Score() != null) {
                    
                    StandingEntry team1Standing = standingsMap.get(match.getTeam1().getId());
                    StandingEntry team2Standing = standingsMap.get(match.getTeam2().getId());
                    
                    if (team1Standing != null && team2Standing != null) {
                        int score1 = match.getTeam1Score();
                        int score2 = match.getTeam2Score();
                        
                        team1Standing.addMatch(score1, score2);
                        team2Standing.addMatch(score2, score1);
                    }
                }
            }
            
            // Save standings to database (update TeamStats)
            for (Team team : tournament.getTeams()) {
                StandingEntry standing = standingsMap.get(team.getId());
                if (standing != null) {
                    // Find or create TeamStats for this team in this tournament
                    TypedQuery<TeamStats> query = em.createQuery(
                        "SELECT ts FROM TeamStats ts WHERE ts.team.id = :teamId AND ts.tournament.id = :tournamentId",
                        TeamStats.class
                    );
                    query.setParameter("teamId", team.getId());
                    query.setParameter("tournamentId", tournament.getId());
                    List<TeamStats> statsList = query.getResultList();
                    
                    TeamStats stats;
                    if (statsList.isEmpty()) {
                        stats = new TeamStats();
                        stats.setTeam(em.find(Team.class, team.getId()));
                        stats.setTournament(em.find(Tournament.class, tournament.getId()));
                        em.persist(stats);
                    } else {
                        stats = statsList.get(0);
                    }
                    
                    // Update stats
                    stats.setMatchesPlayed(standing.getPlayed());
                    stats.setWins(standing.getWins());
                    stats.setLosses(standing.getLosses());
                    stats.setDraws(standing.getDraws());
                    stats.setPoints(standing.getPoints());
                    stats.setGoalsFor(standing.getGoalsFor());
                    stats.setGoalsAgainst(standing.getGoalsAgainst());
                    
                    em.merge(stats);
                }
            }
            
            em.getTransaction().commit();
            
            // Sort standings by points (descending), then goal difference
            List<StandingEntry> sortedStandings = new ArrayList<>(standingsMap.values());
            sortedStandings.sort((a, b) -> {
                int pointsDiff = b.getPoints() - a.getPoints();
                if (pointsDiff != 0) return pointsDiff;
                return b.getGoalDifference() - a.getGoalDifference();
            });
            
            // Assign ranks
            for (int i = 0; i < sortedStandings.size(); i++) {
                sortedStandings.get(i).setRank(i + 1);
            }
            
            // Display standings
            ObservableList<StandingEntry> standings = FXCollections.observableArrayList(sortedStandings);
            standingsTable.setItems(standings);
            
            statusLabel.setText("Standings computed and saved successfully!");
            showAlert("Success", "Tournament standings have been computed and saved to the database.", 
                     Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            showAlert("Error", "Failed to compute standings: " + e.getMessage(), Alert.AlertType.ERROR);
        } finally {
            em.close();
        }
    }
    
    /**
     * Handles export of standings report.
     * TODO: Implement PDF export functionality using a PDF library (e.g., iText, Apache PDFBox)
     */
    @FXML
    private void handleExport() {
        if (standingsTable.getItems().isEmpty()) {
            showAlert("No Data", "Please compute standings first.", Alert.AlertType.WARNING);
            return;
        }
        
        statusLabel.setText("Export functionality requires additional PDF library implementation");
        showAlert("Export Not Implemented", 
                 "Export functionality requires PDF library integration.\nStandings are saved to database and can be viewed here.", 
                 Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void handleRefresh() {
        loadTournaments();
        standingsTable.getItems().clear();
        tournamentInfoLabel.setText("");
        statusLabel.setText("Data refreshed");
    }
    
    @FXML
    private void handleClose() {
        Stage stage = (Stage) tournamentComboBox.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Data class for standing entries.
     */
    public static class StandingEntry {
        private final SimpleIntegerProperty rank = new SimpleIntegerProperty();
        private final SimpleStringProperty teamName;
        private final SimpleIntegerProperty played = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty wins = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty losses = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty draws = new SimpleIntegerProperty(0);
        private final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
        private final SimpleStringProperty goals = new SimpleStringProperty("0:0");
        private int goalsFor = 0;
        private int goalsAgainst = 0;
        
        public StandingEntry(String teamName) {
            this.teamName = new SimpleStringProperty(teamName);
        }
        
        public void addMatch(int scored, int conceded) {
            played.set(played.get() + 1);
            goalsFor += scored;
            goalsAgainst += conceded;
            
            if (scored > conceded) {
                wins.set(wins.get() + 1);
                points.set(points.get() + 3); // 3 points for a win
            } else if (scored < conceded) {
                losses.set(losses.get() + 1);
            } else {
                draws.set(draws.get() + 1);
                points.set(points.get() + 1); // 1 point for a draw
            }
            
            goals.set(goalsFor + ":" + goalsAgainst);
        }
        
        public int getGoalDifference() {
            return goalsFor - goalsAgainst;
        }
        
        // Getters
        public int getRank() { return rank.get(); }
        public void setRank(int rank) { this.rank.set(rank); }
        public String getTeamName() { return teamName.get(); }
        public int getPlayed() { return played.get(); }
        public int getWins() { return wins.get(); }
        public int getLosses() { return losses.get(); }
        public int getDraws() { return draws.get(); }
        public int getPoints() { return points.get(); }
        public String getGoals() { return goals.get(); }
        public int getGoalsFor() { return goalsFor; }
        public int getGoalsAgainst() { return goalsAgainst; }
    }
}
