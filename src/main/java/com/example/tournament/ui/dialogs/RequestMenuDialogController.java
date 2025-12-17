package com.example.tournament.ui.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Controller for the Request Menu Dialog.
 */
public class RequestMenuDialogController {
    
    @FXML
    private ComboBox<String> requestTypeComboBox;
    
    @FXML
    private ComboBox<String> statusFilterComboBox;
    
    @FXML
    private TableView<RequestData> requestsTableView;
    
    @FXML
    private TableColumn<RequestData, Integer> idColumn;
    
    @FXML
    private TableColumn<RequestData, String> typeColumn;
    
    @FXML
    private TableColumn<RequestData, String> requesterColumn;
    
    @FXML
    private TableColumn<RequestData, String> dateColumn;
    
    @FXML
    private TableColumn<RequestData, String> statusColumn;
    
    @FXML
    private TextArea requestDetailsArea;
    
    @FXML
    private TextArea responseArea;
    
    private ObservableList<RequestData> allRequests;
    
    /**
     * Initialize the dialog with sample data.
     */
    @FXML
    public void initialize() {
        // Set up filters
        requestTypeComboBox.setItems(FXCollections.observableArrayList(
            "All Requests", "Team Registration", "Schedule Change", "Venue Change", "Postponement"
        ));
        requestTypeComboBox.setValue("All Requests");
        
        statusFilterComboBox.setItems(FXCollections.observableArrayList(
            "All", "Pending", "Approved", "Rejected"
        ));
        statusFilterComboBox.setValue("All");
        
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        requesterColumn.setCellValueFactory(new PropertyValueFactory<>("requester"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Create sample data
        allRequests = FXCollections.observableArrayList(
            new RequestData(1, "Team Registration", "Team Omega", "Dec 15, 2025", "Pending"),
            new RequestData(2, "Schedule Change", "Team Alpha", "Dec 16, 2025", "Pending"),
            new RequestData(3, "Venue Change", "Team Beta", "Dec 14, 2025", "Approved"),
            new RequestData(4, "Postponement", "Team Gamma", "Dec 17, 2025", "Pending"),
            new RequestData(5, "Team Registration", "Team Theta", "Dec 13, 2025", "Rejected")
        );
        
        requestsTableView.setItems(allRequests);
        
        // Set up selection listener
        requestsTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> updateRequestDetails(newValue)
        );
    }
    
    /**
     * Handle filter change.
     */
    @FXML
    private void handleFilterChange() {
        String typeFilter = requestTypeComboBox.getValue();
        String statusFilter = statusFilterComboBox.getValue();
        
        ObservableList<RequestData> filtered = FXCollections.observableArrayList();
        
        for (RequestData request : allRequests) {
            boolean typeMatch = typeFilter.equals("All Requests") || request.getType().equals(typeFilter);
            boolean statusMatch = statusFilter.equals("All") || request.getStatus().equals(statusFilter);
            
            if (typeMatch && statusMatch) {
                filtered.add(request);
            }
        }
        
        requestsTableView.setItems(filtered);
    }
    
    /**
     * Handle refresh button click.
     */
    @FXML
    private void handleRefresh() {
        // In a real application, this would reload data from the database
        handleFilterChange();
        showAlert("Refreshed", "Request data has been refreshed.", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Update request details area.
     */
    private void updateRequestDetails(RequestData request) {
        if (request != null) {
            requestDetailsArea.setText(
                "Request ID: " + request.getId() + "\n" +
                "Type: " + request.getType() + "\n" +
                "Requester: " + request.getRequester() + "\n" +
                "Date: " + request.getDate() + "\n" +
                "Status: " + request.getStatus() + "\n\n" +
                "Details:\n" +
                getSampleDetails(request.getType())
            );
        }
    }
    
    /**
     * Get sample details based on request type.
     */
    private String getSampleDetails(String type) {
        switch (type) {
            case "Team Registration":
                return "Team requesting registration for Winter Championship 2025.\n" +
                       "11 players registered. All documentation complete.";
            case "Schedule Change":
                return "Team requesting to reschedule match from Dec 20 to Dec 22.\n" +
                       "Reason: Player availability conflicts.";
            case "Venue Change":
                return "Request to change venue from Stadium A to Stadium B.\n" +
                       "Reason: Stadium A undergoing maintenance.";
            case "Postponement":
                return "Request to postpone match by one week.\n" +
                       "Reason: Weather concerns.";
            default:
                return "No additional details available.";
        }
    }
    
    /**
     * Handle approve button click.
     */
    @FXML
    private void handleApprove() {
        RequestData selected = requestsTableView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("No Selection", "Please select a request to approve.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!selected.getStatus().equals("Pending")) {
            showAlert("Invalid Action", "Only pending requests can be approved.", Alert.AlertType.WARNING);
            return;
        }
        
        // Update status
        selected.setStatus("Approved");
        requestsTableView.refresh();
        
        String response = responseArea.getText();
        showAlert("Approved", "Request #" + selected.getId() + " has been approved.\n\n" +
                 (response.isEmpty() ? "" : "Response: " + response), Alert.AlertType.INFORMATION);
        
        responseArea.clear();
    }
    
    /**
     * Handle reject button click.
     */
    @FXML
    private void handleReject() {
        RequestData selected = requestsTableView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("No Selection", "Please select a request to reject.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!selected.getStatus().equals("Pending")) {
            showAlert("Invalid Action", "Only pending requests can be rejected.", Alert.AlertType.WARNING);
            return;
        }
        
        // Update status
        selected.setStatus("Rejected");
        requestsTableView.refresh();
        
        String response = responseArea.getText();
        showAlert("Rejected", "Request #" + selected.getId() + " has been rejected.\n\n" +
                 (response.isEmpty() ? "" : "Reason: " + response), Alert.AlertType.INFORMATION);
        
        responseArea.clear();
    }
    
    /**
     * Handle view details button click.
     */
    @FXML
    private void handleViewDetails() {
        RequestData selected = requestsTableView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("No Selection", "Please select a request to view details.", Alert.AlertType.WARNING);
            return;
        }
        
        updateRequestDetails(selected);
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) requestsTableView.getScene().getWindow();
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
     * Data class for request information.
     */
    public static class RequestData {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty type;
        private final SimpleStringProperty requester;
        private final SimpleStringProperty date;
        private final SimpleStringProperty status;
        
        public RequestData(int id, String type, String requester, String date, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.type = new SimpleStringProperty(type);
            this.requester = new SimpleStringProperty(requester);
            this.date = new SimpleStringProperty(date);
            this.status = new SimpleStringProperty(status);
        }
        
        public int getId() { return id.get(); }
        public String getType() { return type.get(); }
        public String getRequester() { return requester.get(); }
        public String getDate() { return date.get(); }
        public String getStatus() { return status.get(); }
        
        public void setStatus(String status) { this.status.set(status); }
    }
}
