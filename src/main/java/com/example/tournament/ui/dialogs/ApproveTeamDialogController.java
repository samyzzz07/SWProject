package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Approve Team Dialog.
 */
public class ApproveTeamDialogController {
    
    @FXML
    private ComboBox<Team> teamComboBox;
    
    @FXML
    private Label teamNameLabel;
    
    @FXML
    private Label managerLabel;
    
    @FXML
    private Label contactLabel;
    
    @FXML
    private Label playersLabel;
    
    @FXML
    private Label registrationDateLabel;
    
    @FXML
    private TextArea notesArea;
    
    // Service for team operations
    private TeamService teamService = new TeamService();
    
    /**
     * Initialize the dialog with teams from database.
     */
    @FXML
    public void initialize() {
        // Load pending teams from database
        loadPendingTeams();
        
        // Configure the ComboBox to display team names
        teamComboBox.setConverter(new javafx.util.StringConverter<Team>() {
            @Override
            public String toString(Team team) {
                return team != null ? team.getName() : "";
            }
            
            @Override
            public Team fromString(String string) {
                return null; // Not needed for this use case
            }
        });
    }
    
    /**
     * Load teams with PENDING approval status from the database.
     */
    private void loadPendingTeams() {
        try {
            List<Team> allTeams = teamService.getAllTeams();
            List<Team> pendingTeams = allTeams.stream()
                .filter(team -> "PENDING".equals(team.getApprovalStatus()))
                .collect(Collectors.toList());
            
            ObservableList<Team> teamList = FXCollections.observableArrayList(pendingTeams);
            teamComboBox.setItems(teamList);
            
            System.out.println("Loaded " + pendingTeams.size() + " pending teams for approval");
        } catch (Exception e) {
            System.err.println("Error loading pending teams: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load pending teams: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle team selection change.
     */
    @FXML
    private void handleTeamSelection() {
        Team selectedTeam = teamComboBox.getValue();
        if (selectedTeam != null) {
            // Display team information
            teamNameLabel.setText(selectedTeam.getName());
            managerLabel.setText(selectedTeam.getManager() != null ? 
                selectedTeam.getManager().getUsername() : "N/A");
            contactLabel.setText(selectedTeam.getContactInfo() != null ? 
                selectedTeam.getContactInfo() : "N/A");
            playersLabel.setText(selectedTeam.getPlayers().size() + " registered players");
            registrationDateLabel.setText("N/A"); // Could add a registration date field to Team if needed
        } else {
            clearTeamInfo();
        }
    }
    
    /**
     * Clear team information display.
     */
    private void clearTeamInfo() {
        teamNameLabel.setText("-");
        managerLabel.setText("-");
        contactLabel.setText("-");
        playersLabel.setText("-");
        registrationDateLabel.setText("-");
    }
    
    /**
     * Handle approve button click.
     */
    @FXML
    private void handleApprove() {
        Team selectedTeam = teamComboBox.getValue();
        
        if (selectedTeam == null) {
            showAlert("No Team Selected", "Please select a team to approve.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm approval
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Approval");
        confirm.setHeaderText("Approve " + selectedTeam.getName() + "?");
        confirm.setContentText("Are you sure you want to approve this team for tournament participation?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Update the approval status
                    selectedTeam.setApprovalStatus("APPROVED");
                    
                    // Persist to database
                    boolean success = teamService.updateTeam(selectedTeam);
                    
                    if (success) {
                        String notes = notesArea.getText();
                        showAlert("Success", selectedTeam.getName() + " has been approved for tournament participation!\n\n" +
                                 (notes.isEmpty() ? "" : "Notes: " + notes), Alert.AlertType.INFORMATION);
                        
                        // Remove from pending list
                        teamComboBox.getItems().remove(selectedTeam);
                        teamComboBox.setValue(null);
                        clearTeamInfo();
                        notesArea.clear();
                    } else {
                        showAlert("Error", "Failed to save approval status to database.", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    showAlert("Error", "Failed to approve team: " + e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Handle reject button click.
     */
    @FXML
    private void handleReject() {
        Team selectedTeam = teamComboBox.getValue();
        
        if (selectedTeam == null) {
            showAlert("No Team Selected", "Please select a team to reject.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm rejection
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Rejection");
        confirm.setHeaderText("Reject " + selectedTeam.getName() + "?");
        confirm.setContentText("Are you sure you want to reject this team's registration?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Update the approval status
                    selectedTeam.setApprovalStatus("REJECTED");
                    
                    // Persist to database
                    boolean success = teamService.updateTeam(selectedTeam);
                    
                    if (success) {
                        String notes = notesArea.getText();
                        showAlert("Rejected", selectedTeam.getName() + " has been rejected.\n\n" +
                                 (notes.isEmpty() ? "" : "Reason: " + notes), Alert.AlertType.INFORMATION);
                        
                        // Remove from pending list
                        teamComboBox.getItems().remove(selectedTeam);
                        teamComboBox.setValue(null);
                        clearTeamInfo();
                        notesArea.clear();
                    } else {
                        showAlert("Error", "Failed to save rejection status to database.", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    showAlert("Error", "Failed to reject team: " + e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) teamComboBox.getScene().getWindow();
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
