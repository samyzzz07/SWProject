package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

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
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Set up status filter
        statusFilterComboBox.setItems(FXCollections.observableArrayList(
            "All", "Approved", "Pending", "Rejected"
        ));
        statusFilterComboBox.setValue("All");
        
        // Set up table columns
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Create sample data
        allTeams = FXCollections.observableArrayList(
            new TeamData("Team Alpha", "John Manager", "alpha@team.com", "Approved"),
            new TeamData("Team Beta", "Jane Leader", "beta@team.com", "Pending"),
            new TeamData("Team Gamma", "Mike Coach", "gamma@team.com", "Approved"),
            new TeamData("Team Delta", "Sarah Director", "delta@team.com", "Pending"),
            new TeamData("Team Epsilon", "David Captain", "epsilon@team.com", "Approved"),
            new TeamData("Team Zeta", "Emily Boss", "zeta@team.com", "Rejected")
        );
        
        teamsTableView.setItems(allTeams);
        
        // Set up selection listener
        teamsTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> updateTeamDetails(newValue)
        );
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
        // In a real application, this would reload data from the database
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
                "Players: 11\n" +
                "Registration Date: Dec 1, 2025\n" +
                "Last Updated: Dec 15, 2025"
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
        
        public TeamData(String teamName, String manager, String contact, String status) {
            this.teamName = new SimpleStringProperty(teamName);
            this.manager = new SimpleStringProperty(manager);
            this.contact = new SimpleStringProperty(contact);
            this.status = new SimpleStringProperty(status);
        }
        
        public String getTeamName() { return teamName.get(); }
        public String getManager() { return manager.get(); }
        public String getContact() { return contact.get(); }
        public String getStatus() { return status.get(); }
    }
}
