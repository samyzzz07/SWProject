package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller for the Find Sponsor Dialog.
 */
public class FindSponsorDialogController {
    
    @FXML
    private ListView<String> sponsorsListView;
    
    @FXML
    private TextField companyNameField;
    
    @FXML
    private TextField contactPersonField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField contributionField;
    
    @FXML
    private ComboBox<String> sponsorTypeComboBox;
    
    @FXML
    private TextArea notesArea;
    
    private ObservableList<String> sponsorsList;
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Populate current sponsors
        sponsorsList = FXCollections.observableArrayList(
            "Sports Inc. - Platinum ($10,000)",
            "Athletic Gear Co. - Gold ($7,500)",
            "Tournament Partners LLC - Silver ($5,000)"
        );
        sponsorsListView.setItems(sponsorsList);
        
        // Populate sponsor types
        sponsorTypeComboBox.setItems(FXCollections.observableArrayList(
            "Platinum Sponsor",
            "Gold Sponsor",
            "Silver Sponsor",
            "Bronze Sponsor",
            "Media Partner",
            "Equipment Sponsor"
        ));
    }
    
    /**
     * Handle add sponsor button click.
     */
    @FXML
    private void handleAddSponsor() {
        String companyName = companyNameField.getText();
        String contactPerson = contactPersonField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String contribution = contributionField.getText();
        String type = sponsorTypeComboBox.getValue();
        
        // Validate inputs
        if (companyName == null || companyName.trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the company name.", Alert.AlertType.WARNING);
            return;
        }
        
        if (contactPerson == null || contactPerson.trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the contact person name.", Alert.AlertType.WARNING);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the email address.", Alert.AlertType.WARNING);
            return;
        }
        
        if (contribution == null || contribution.trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the contribution amount.", Alert.AlertType.WARNING);
            return;
        }
        
        if (type == null) {
            showAlert("Missing Information", "Please select the sponsor type.", Alert.AlertType.WARNING);
            return;
        }
        
        // Add to sponsors list
        String sponsorEntry = companyName + " - " + type + " ($" + contribution + ")";
        sponsorsList.add(sponsorEntry);
        
        showAlert("Success", "Sponsor added successfully!\n\n" +
                 "Company: " + companyName + "\n" +
                 "Contact: " + contactPerson + "\n" +
                 "Email: " + email + "\n" +
                 "Phone: " + phone + "\n" +
                 "Type: " + type + "\n" +
                 "Contribution: $" + contribution, Alert.AlertType.INFORMATION);
        
        // Clear fields
        clearFields();
    }
    
    /**
     * Handle remove sponsor button click.
     */
    @FXML
    private void handleRemoveSponsor() {
        String selectedSponsor = sponsorsListView.getSelectionModel().getSelectedItem();
        
        if (selectedSponsor == null) {
            showAlert("No Selection", "Please select a sponsor to remove.", Alert.AlertType.WARNING);
            return;
        }
        
        // Confirm removal
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove Sponsor?");
        confirm.setContentText("Are you sure you want to remove " + selectedSponsor + "?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                sponsorsList.remove(selectedSponsor);
                showAlert("Removed", "Sponsor has been removed.", Alert.AlertType.INFORMATION);
            }
        });
    }
    
    /**
     * Clear all input fields.
     */
    private void clearFields() {
        companyNameField.clear();
        contactPersonField.clear();
        emailField.clear();
        phoneField.clear();
        contributionField.clear();
        sponsorTypeComboBox.setValue(null);
        notesArea.clear();
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) sponsorsListView.getScene().getWindow();
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
