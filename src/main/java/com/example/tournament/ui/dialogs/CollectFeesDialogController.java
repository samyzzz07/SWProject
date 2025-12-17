package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controller for the Collect Fees Dialog.
 */
public class CollectFeesDialogController {
    
    @FXML
    private Label totalTeamsLabel;
    
    @FXML
    private Label paidCountLabel;
    
    @FXML
    private Label pendingCountLabel;
    
    @FXML
    private Label totalCollectedLabel;
    
    @FXML
    private TableView<FeeData> feesTableView;
    
    @FXML
    private TableColumn<FeeData, String> teamNameColumn;
    
    @FXML
    private TableColumn<FeeData, String> amountColumn;
    
    @FXML
    private TableColumn<FeeData, String> statusColumn;
    
    @FXML
    private TableColumn<FeeData, String> paymentDateColumn;
    
    @FXML
    private ComboBox<String> teamComboBox;
    
    @FXML
    private TextField amountField;
    
    private ObservableList<FeeData> feeDataList;
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Set up table columns
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        
        // Create sample data
        feeDataList = FXCollections.observableArrayList(
            new FeeData("Team Alpha", "$500", "Paid", "Dec 10, 2025"),
            new FeeData("Team Beta", "$500", "Pending", "-"),
            new FeeData("Team Gamma", "$500", "Paid", "Dec 12, 2025"),
            new FeeData("Team Delta", "$500", "Pending", "-"),
            new FeeData("Team Epsilon", "$500", "Paid", "Dec 15, 2025"),
            new FeeData("Team Zeta", "$500", "Pending", "-")
        );
        
        feesTableView.setItems(feeDataList);
        
        // Update summary
        updateSummary();
        
        // Populate team combo box with pending teams
        updateTeamComboBox();
    }
    
    /**
     * Update summary labels.
     */
    private void updateSummary() {
        int total = feeDataList.size();
        int paid = 0;
        int pending = 0;
        double totalCollected = 0.0;
        
        for (FeeData fee : feeDataList) {
            if (fee.getStatus().equals("Paid")) {
                paid++;
                totalCollected += 500.0; // Assuming $500 per team
            } else {
                pending++;
            }
        }
        
        totalTeamsLabel.setText(String.valueOf(total));
        paidCountLabel.setText(String.valueOf(paid));
        pendingCountLabel.setText(String.valueOf(pending));
        totalCollectedLabel.setText("$" + String.format("%.2f", totalCollected));
    }
    
    /**
     * Update team combo box with pending teams.
     */
    private void updateTeamComboBox() {
        ObservableList<String> pendingTeams = FXCollections.observableArrayList();
        for (FeeData fee : feeDataList) {
            if (fee.getStatus().equals("Pending")) {
                pendingTeams.add(fee.getTeamName());
            }
        }
        teamComboBox.setItems(pendingTeams);
    }
    
    /**
     * Handle mark as paid button click.
     */
    @FXML
    private void handleMarkAsPaid() {
        String selectedTeam = teamComboBox.getValue();
        String amount = amountField.getText();
        
        if (selectedTeam == null) {
            showAlert("No Team Selected", "Please select a team to record payment.", Alert.AlertType.WARNING);
            return;
        }
        
        if (amount == null || amount.trim().isEmpty()) {
            showAlert("No Amount Entered", "Please enter the payment amount.", Alert.AlertType.WARNING);
            return;
        }
        
        // Find and update the fee record
        for (FeeData fee : feeDataList) {
            if (fee.getTeamName().equals(selectedTeam)) {
                fee.setStatus("Paid");
                fee.setPaymentDate(java.time.LocalDate.now().toString());
                break;
            }
        }
        
        // Refresh the table
        feesTableView.refresh();
        updateSummary();
        updateTeamComboBox();
        
        // Clear fields
        teamComboBox.setValue(null);
        amountField.clear();
        
        showAlert("Success", "Payment recorded for " + selectedTeam + "!\n\n" +
                 "Amount: $" + amount, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle export button click.
     */
    @FXML
    private void handleExport() {
        showAlert("Export", "Fee collection report has been exported to fees_report.pdf", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle refresh button click.
     */
    @FXML
    private void handleRefresh() {
        // In a real application, this would reload data from the database
        updateSummary();
        showAlert("Refreshed", "Fee data has been refreshed.", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) feesTableView.getScene().getWindow();
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
     * Data class for fee information.
     */
    public static class FeeData {
        private final SimpleStringProperty teamName;
        private final SimpleStringProperty amount;
        private final SimpleStringProperty status;
        private final SimpleStringProperty paymentDate;
        
        public FeeData(String teamName, String amount, String status, String paymentDate) {
            this.teamName = new SimpleStringProperty(teamName);
            this.amount = new SimpleStringProperty(amount);
            this.status = new SimpleStringProperty(status);
            this.paymentDate = new SimpleStringProperty(paymentDate);
        }
        
        public String getTeamName() { return teamName.get(); }
        public String getAmount() { return amount.get(); }
        public String getStatus() { return status.get(); }
        public String getPaymentDate() { return paymentDate.get(); }
        
        public void setStatus(String status) { this.status.set(status); }
        public void setPaymentDate(String date) { this.paymentDate.set(date); }
    }
}
