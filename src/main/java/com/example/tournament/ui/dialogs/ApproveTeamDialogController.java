package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller for the Approve Team Dialog.
 */
public class ApproveTeamDialogController {
    
    @FXML
    private ComboBox<String> teamComboBox;
    
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
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Populate pending teams
        ObservableList<String> pendingTeams = FXCollections.observableArrayList(
            "Team Beta",
            "Team Delta",
            "Team Theta",
            "Team Omega"
        );
        teamComboBox.setItems(pendingTeams);
    }
    
    /**
     * Handle team selection change.
     */
    @FXML
    private void handleTeamSelection() {
        String selectedTeam = teamComboBox.getValue();
        if (selectedTeam != null) {
            // Display sample team information based on selection
            switch (selectedTeam) {
                case "Team Beta":
                    teamNameLabel.setText("Team Beta");
                    managerLabel.setText("Jane Leader");
                    contactLabel.setText("beta@team.com | (555) 123-4567");
                    playersLabel.setText("11 registered players");
                    registrationDateLabel.setText("December 5, 2025");
                    break;
                case "Team Delta":
                    teamNameLabel.setText("Team Delta");
                    managerLabel.setText("Sarah Director");
                    contactLabel.setText("delta@team.com | (555) 234-5678");
                    playersLabel.setText("10 registered players");
                    registrationDateLabel.setText("December 8, 2025");
                    break;
                case "Team Theta":
                    teamNameLabel.setText("Team Theta");
                    managerLabel.setText("Robert Coach");
                    contactLabel.setText("theta@team.com | (555) 345-6789");
                    playersLabel.setText("12 registered players");
                    registrationDateLabel.setText("December 10, 2025");
                    break;
                case "Team Omega":
                    teamNameLabel.setText("Team Omega");
                    managerLabel.setText("Lisa Manager");
                    contactLabel.setText("omega@team.com | (555) 456-7890");
                    playersLabel.setText("11 registered players");
                    registrationDateLabel.setText("December 12, 2025");
                    break;
                default:
                    clearTeamInfo();
            }
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
        String selectedTeam = teamComboBox.getValue();
        
        if (selectedTeam == null) {
            showAlert("No Team Selected", "Please select a team to approve.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm approval
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Approval");
        confirm.setHeaderText("Approve " + selectedTeam + "?");
        confirm.setContentText("Are you sure you want to approve this team for tournament participation?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // In a real application, this would update the database
                String notes = notesArea.getText();
                showAlert("Success", selectedTeam + " has been approved for tournament participation!\n\n" +
                         (notes.isEmpty() ? "" : "Notes: " + notes), Alert.AlertType.INFORMATION);
                
                // Remove from pending list
                teamComboBox.getItems().remove(selectedTeam);
                teamComboBox.setValue(null);
                clearTeamInfo();
                notesArea.clear();
            }
        });
    }
    
    /**
     * Handle reject button click.
     */
    @FXML
    private void handleReject() {
        String selectedTeam = teamComboBox.getValue();
        
        if (selectedTeam == null) {
            showAlert("No Team Selected", "Please select a team to reject.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm rejection
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Rejection");
        confirm.setHeaderText("Reject " + selectedTeam + "?");
        confirm.setContentText("Are you sure you want to reject this team's registration?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String notes = notesArea.getText();
                showAlert("Rejected", selectedTeam + " has been rejected.\n\n" +
                         (notes.isEmpty() ? "" : "Reason: " + notes), Alert.AlertType.INFORMATION);
                
                // Remove from pending list
                teamComboBox.getItems().remove(selectedTeam);
                teamComboBox.setValue(null);
                clearTeamInfo();
                notesArea.clear();
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
