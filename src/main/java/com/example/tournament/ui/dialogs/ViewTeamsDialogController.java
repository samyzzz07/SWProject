package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

/**
 * Controller for the View Teams Dialog.
 */
public class ViewTeamsDialogController {
    
    @FXML
    private ComboBox<String> statusFilterComboBox;
    
    @FXML
    private TableView<TeamData> teamsTableView;
    
    @FXML
    private TableColumn<TeamData, String> teamNameColumn;
    
    @FXML
    private TableColumn<TeamData, String> managerColumn;
    
    @FXML
    private TableColumn<TeamData, String> contactColumn;
    
    @FXML
    private TableColumn<TeamData, String> statusColumn;
    
    @FXML
    private TextArea teamDetailsArea;
    
    private ObservableList<TeamData> allTeams;
    private TeamService teamService;
    
    /**
     * Initialize the dialog with data from the database.
     */
    @FXML
    public void initialize() {
        teamService = new TeamService();
        
        // Set up status filter
        statusFilterComboBox.setItems(FXCollections.observableArrayList(
            "All", "APPROVED", "PENDING", "REJECTED", "NOT_REQUESTED"
        ));
        statusFilterComboBox.setValue("All");
        
        // Set up table columns
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Load teams from database
        loadTeamsFromDatabase();
        
        teamsTableView.setItems(allTeams);
        
        // Set up selection listener
        teamsTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> updateTeamDetails(newValue)
        );
    }
    
    /**
     * Load teams from the database.
     */
    private void loadTeamsFromDatabase() {
        allTeams = FXCollections.observableArrayList();
        
        try {
            List<Team> teams = teamService.getAllTeams();
            
            for (Team team : teams) {
                String managerName = team.getManager() != null ? team.getManager().getUsername() : "No Manager";
                String contact = team.getContactInfo() != null ? team.getContactInfo() : "N/A";
                String status = team.getApprovalStatus() != null ? team.getApprovalStatus() : "NOT_REQUESTED";
                
                allTeams.add(new TeamData(
                    team.getName(),
                    managerName,
                    contact,
                    status,
                    team.getPlayers().size()
                ));
            }
            
            System.out.println("Loaded " + teams.size() + " teams from database");
        } catch (Exception e) {
            System.err.println("Error loading teams from database: " + e.getMessage());
            e.printStackTrace();
            
            // Show error in details area
            if (teamDetailsArea != null) {
                teamDetailsArea.setText("Error loading teams: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handle filter change.
     */
    @FXML
    private void handleFilterChange() {
        String filter = statusFilterComboBox.getValue();
        if (filter == null || filter.equals("All")) {
            teamsTableView.setItems(allTeams);
        } else {
            ObservableList<TeamData> filtered = FXCollections.observableArrayList();
            for (TeamData team : allTeams) {
                if (team.getStatus().equals(filter)) {
                    filtered.add(team);
                }
            }
            teamsTableView.setItems(filtered);
        }
    }
    
    /**
     * Handle refresh button click.
     */
    @FXML
    private void handleRefresh() {
        // Reload data from the database
        loadTeamsFromDatabase();
        teamsTableView.setItems(allTeams);
        handleFilterChange(); // Reapply current filter
        teamDetailsArea.setText("Data refreshed successfully.");
    }
    
    /**
     * Handle view details button click.
     */
    @FXML
    private void handleViewDetails() {
        TeamData selected = teamsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a team to view details.", Alert.AlertType.WARNING);
            return;
        }
        updateTeamDetails(selected);
    }
    
    /**
     * Update team details area.
     */
    private void updateTeamDetails(TeamData team) {
        if (team != null) {
            teamDetailsArea.setText(
                "Team Name: " + team.getTeamName() + "\n" +
                "Manager: " + team.getManager() + "\n" +
                "Contact: " + team.getContact() + "\n" +
                "Status: " + team.getStatus() + "\n" +
                "Players: " + team.getPlayerCount() + "\n"
            );
        }
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) teamsTableView.getScene().getWindow();
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
     * Data class for team information.
     */
    public static class TeamData {
        private final SimpleStringProperty teamName;
        private final SimpleStringProperty manager;
        private final SimpleStringProperty contact;
        private final SimpleStringProperty status;
        private final int playerCount;
        
        public TeamData(String teamName, String manager, String contact, String status, int playerCount) {
            this.teamName = new SimpleStringProperty(teamName);
            this.manager = new SimpleStringProperty(manager);
            this.contact = new SimpleStringProperty(contact);
            this.status = new SimpleStringProperty(status);
            this.playerCount = playerCount;
        }
        
        public String getTeamName() { return teamName.get(); }
        public String getManager() { return manager.get(); }
        public String getContact() { return contact.get(); }
        public String getStatus() { return status.get(); }
        public int getPlayerCount() { return playerCount; }
    }
}
