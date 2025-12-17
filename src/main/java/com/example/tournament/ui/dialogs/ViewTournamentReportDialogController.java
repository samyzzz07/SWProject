package com.example.tournament.ui.dialogs;

import com.example.tournament.model.*;
import com.example.tournament.service.TournamentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the View Tournament Report Dialog.
 */
public class ViewTournamentReportDialogController {
    
    @FXML
    private ComboBox<String> tournamentComboBox;
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label sportLabel;
    
    @FXML
    private Label typeLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label startDateLabel;
    
    @FXML
    private Label endDateLabel;
    
    @FXML
    private Label totalTeamsLabel;
    
    @FXML
    private Label totalMatchesLabel;
    
    @FXML
    private Label completedMatchesLabel;
    
    @FXML
    private Label upcomingMatchesLabel;
    
    @FXML
    private Label totalGoalsLabel;
    
    @FXML
    private Label averageScoreLabel;
    
    @FXML
    private Label completionLabel;
    
    @FXML
    private TableView<StandingData> standingsTableView;
    
    @FXML
    private TableColumn<StandingData, Integer> positionColumn;
    
    @FXML
    private TableColumn<StandingData, String> teamNameColumn;
    
    @FXML
    private TableColumn<StandingData, Integer> matchesPlayedColumn;
    
    @FXML
    private TableColumn<StandingData, Integer> winsColumn;
    
    @FXML
    private TableColumn<StandingData, Integer> drawsColumn;
    
    @FXML
    private TableColumn<StandingData, Integer> lossesColumn;
    
    @FXML
    private TableColumn<StandingData, Integer> pointsColumn;
    
    @FXML
    private TableView<MatchData> matchesTableView;
    
    @FXML
    private TableColumn<MatchData, String> matchDateColumn;
    
    @FXML
    private TableColumn<MatchData, String> team1Column;
    
    @FXML
    private TableColumn<MatchData, String> scoreColumn;
    
    @FXML
    private TableColumn<MatchData, String> team2Column;
    
    @FXML
    private TableColumn<MatchData, String> matchStatusColumn;
    
    private TournamentService tournamentService;
    private Tournament selectedTournament;
    
    /**
     * Initialize the dialog.
     */
    @FXML
    public void initialize() {
        tournamentService = new TournamentService();
        
        // Set up standings table columns
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        matchesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("matchesPlayed"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
        drawsColumn.setCellValueFactory(new PropertyValueFactory<>("draws"));
        lossesColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        // Set up matches table columns
        matchDateColumn.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        team1Column.setCellValueFactory(new PropertyValueFactory<>("team1"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        team2Column.setCellValueFactory(new PropertyValueFactory<>("team2"));
        matchStatusColumn.setCellValueFactory(new PropertyValueFactory<>("matchStatus"));
        
        // Load tournaments
        loadTournaments();
    }
    
    /**
     * Load tournaments from the database.
     */
    private void loadTournaments() {
        try {
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            ObservableList<String> tournamentNames = FXCollections.observableArrayList();
            
            for (Tournament tournament : tournaments) {
                tournamentNames.add(tournament.getName());
            }
            
            tournamentComboBox.setItems(tournamentNames);
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tournaments: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle tournament selection.
     */
    @FXML
    private void handleTournamentSelection() {
        String selectedName = tournamentComboBox.getValue();
        if (selectedName == null) {
            return;
        }
        
        try {
            List<Tournament> tournaments = tournamentService.viewAllTournaments();
            selectedTournament = tournaments.stream()
                .filter(t -> t.getName().equals(selectedName))
                .findFirst()
                .orElse(null);
            
            if (selectedTournament != null) {
                updateReportView();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load tournament details: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Update the report view with tournament data.
     */
    private void updateReportView() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        
        // Overview
        nameLabel.setText(selectedTournament.getName());
        sportLabel.setText(selectedTournament.getSport() != null ? selectedTournament.getSport().getName() : "N/A");
        
        if (selectedTournament instanceof LeagueTournament) {
            typeLabel.setText("League/Round Robin");
        } else if (selectedTournament instanceof RoundRobinTournament) {
            typeLabel.setText("Round Robin");
        } else if (selectedTournament instanceof KnockoutTournament) {
            typeLabel.setText("Knockout");
        } else {
            typeLabel.setText("Unknown");
        }
        
        statusLabel.setText(selectedTournament.getStatus() != null ? 
            selectedTournament.getStatus().toString() : "UNKNOWN");
        startDateLabel.setText(selectedTournament.getStartDate() != null ? 
            selectedTournament.getStartDate().format(dateFormatter) : "N/A");
        endDateLabel.setText(selectedTournament.getEndDate() != null ? 
            selectedTournament.getEndDate().format(dateFormatter) : "N/A");
        
        // Statistics
        int totalTeams = selectedTournament.getTeams().size();
        int totalMatches = selectedTournament.getMatches().size();
        int completedMatches = (int) selectedTournament.getMatches().stream()
            .filter(m -> m.getStatus() == Match.MatchStatus.COMPLETED)
            .count();
        int upcomingMatches = totalMatches - completedMatches;
        
        totalTeamsLabel.setText(String.valueOf(totalTeams));
        totalMatchesLabel.setText(String.valueOf(totalMatches));
        completedMatchesLabel.setText(String.valueOf(completedMatches));
        upcomingMatchesLabel.setText(String.valueOf(upcomingMatches));
        
        // Calculate total goals
        int totalGoals = selectedTournament.getMatches().stream()
            .filter(m -> m.getStatus() == Match.MatchStatus.COMPLETED)
            .mapToInt(m -> {
                int score1 = m.getTeam1Score() != null ? m.getTeam1Score() : 0;
                int score2 = m.getTeam2Score() != null ? m.getTeam2Score() : 0;
                return score1 + score2;
            })
            .sum();
        totalGoalsLabel.setText(String.valueOf(totalGoals));
        
        // Average score
        double averageScore = completedMatches > 0 ? (double) totalGoals / completedMatches : 0.0;
        averageScoreLabel.setText(String.format("%.2f", averageScore));
        
        // Completion percentage
        double completion = totalMatches > 0 ? (double) completedMatches / totalMatches * 100 : 0.0;
        completionLabel.setText(String.format("%.1f%%", completion));
        
        // Populate standings table
        populateStandings();
        
        // Populate matches table
        populateMatches();
    }
    
    /**
     * Populate the standings table.
     */
    private void populateStandings() {
        ObservableList<StandingData> standings = FXCollections.observableArrayList();
        
        if (selectedTournament instanceof LeagueTournament) {
            LeagueTournament league = (LeagueTournament) selectedTournament;
            var standingsMap = league.getStandings();
            
            int position = 1;
            for (var entry : standingsMap.entrySet()) {
                Team team = entry.getKey();
                int points = entry.getValue();
                
                // Calculate team statistics
                int played = 0, wins = 0, draws = 0, losses = 0;
                for (Match match : selectedTournament.getMatches()) {
                    if (match.getStatus() == Match.MatchStatus.COMPLETED &&
                        match.getTeam1Score() != null && match.getTeam2Score() != null &&
                        (match.getTeam1().equals(team) || match.getTeam2().equals(team))) {
                        played++;
                        
                        int score1 = match.getTeam1Score();
                        int score2 = match.getTeam2Score();
                        
                        if (match.getTeam1().equals(team)) {
                            if (score1 > score2) wins++;
                            else if (score1 == score2) draws++;
                            else losses++;
                        } else {
                            if (score2 > score1) wins++;
                            else if (score2 == score1) draws++;
                            else losses++;
                        }
                    }
                }
                
                standings.add(new StandingData(position++, team.getName(), played, wins, draws, losses, points));
            }
        } else if (selectedTournament instanceof RoundRobinTournament) {
            RoundRobinTournament roundRobin = (RoundRobinTournament) selectedTournament;
            var standingsMap = roundRobin.getStandings();
            
            int position = 1;
            for (var entry : standingsMap.entrySet()) {
                Team team = entry.getKey();
                int points = entry.getValue();
                
                // Calculate team statistics
                int played = 0, wins = 0, draws = 0, losses = 0;
                for (Match match : selectedTournament.getMatches()) {
                    if (match.getStatus() == Match.MatchStatus.COMPLETED &&
                        match.getTeam1Score() != null && match.getTeam2Score() != null &&
                        (match.getTeam1().equals(team) || match.getTeam2().equals(team))) {
                        played++;
                        
                        int score1 = match.getTeam1Score();
                        int score2 = match.getTeam2Score();
                        
                        if (match.getTeam1().equals(team)) {
                            if (score1 > score2) wins++;
                            else if (score1 == score2) draws++;
                            else losses++;
                        } else {
                            if (score2 > score1) wins++;
                            else if (score2 == score1) draws++;
                            else losses++;
                        }
                    }
                }
                
                standings.add(new StandingData(position++, team.getName(), played, wins, draws, losses, points));
            }
        } else {
            // For knockout tournaments, show all teams with basic info
            int position = 1;
            for (Team team : selectedTournament.getTeams()) {
                standings.add(new StandingData(position++, team.getName(), 0, 0, 0, 0, 0));
            }
        }
        
        standingsTableView.setItems(standings);
    }
    
    /**
     * Populate the matches table.
     */
    private void populateMatches() {
        ObservableList<MatchData> matches = FXCollections.observableArrayList();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        
        for (Match match : selectedTournament.getMatches()) {
            String date = match.getScheduledTime() != null ? 
                match.getScheduledTime().format(dateFormatter) : "TBD";
            String team1 = match.getTeam1() != null ? match.getTeam1().getName() : "TBD";
            String team2 = match.getTeam2() != null ? match.getTeam2().getName() : "TBD";
            String score = "-";
            
            if (match.getStatus() == Match.MatchStatus.COMPLETED) {
                score = (match.getTeam1Score() != null ? match.getTeam1Score() : 0) + " - " + 
                        (match.getTeam2Score() != null ? match.getTeam2Score() : 0);
            }
            
            String status = match.getStatus() != null ? match.getStatus().toString() : "UNKNOWN";
            
            matches.add(new MatchData(date, team1, score, team2, status));
        }
        
        matchesTableView.setItems(matches);
    }
    
    /**
     * Handle export report button click.
     */
    @FXML
    private void handleExportReport() {
        showAlert("Export Report", "Report export functionality will be implemented.", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle print button click.
     */
    @FXML
    private void handlePrint() {
        showAlert("Print Report", "Report printing functionality will be implemented.", Alert.AlertType.INFORMATION);
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
     * Data class for standing information.
     */
    public static class StandingData {
        private final SimpleIntegerProperty position;
        private final SimpleStringProperty teamName;
        private final SimpleIntegerProperty matchesPlayed;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty draws;
        private final SimpleIntegerProperty losses;
        private final SimpleIntegerProperty points;
        
        public StandingData(int position, String teamName, int matchesPlayed, int wins, int draws, int losses, int points) {
            this.position = new SimpleIntegerProperty(position);
            this.teamName = new SimpleStringProperty(teamName);
            this.matchesPlayed = new SimpleIntegerProperty(matchesPlayed);
            this.wins = new SimpleIntegerProperty(wins);
            this.draws = new SimpleIntegerProperty(draws);
            this.losses = new SimpleIntegerProperty(losses);
            this.points = new SimpleIntegerProperty(points);
        }
        
        public int getPosition() { return position.get(); }
        public String getTeamName() { return teamName.get(); }
        public int getMatchesPlayed() { return matchesPlayed.get(); }
        public int getWins() { return wins.get(); }
        public int getDraws() { return draws.get(); }
        public int getLosses() { return losses.get(); }
        public int getPoints() { return points.get(); }
    }
    
    /**
     * Data class for match information.
     */
    public static class MatchData {
        private final SimpleStringProperty matchDate;
        private final SimpleStringProperty team1;
        private final SimpleStringProperty score;
        private final SimpleStringProperty team2;
        private final SimpleStringProperty matchStatus;
        
        public MatchData(String matchDate, String team1, String score, String team2, String matchStatus) {
            this.matchDate = new SimpleStringProperty(matchDate);
            this.team1 = new SimpleStringProperty(team1);
            this.score = new SimpleStringProperty(score);
            this.team2 = new SimpleStringProperty(team2);
            this.matchStatus = new SimpleStringProperty(matchStatus);
        }
        
        public String getMatchDate() { return matchDate.get(); }
        public String getTeam1() { return team1.get(); }
        public String getScore() { return score.get(); }
        public String getTeam2() { return team2.get(); }
        public String getMatchStatus() { return matchStatus.get(); }
    }
}
