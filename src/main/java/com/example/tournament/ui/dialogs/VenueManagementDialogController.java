package com.example.tournament.ui.dialogs;

import com.example.tournament.model.Venue;
import com.example.tournament.service.VenueService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Controller for the Venue Management Dialog.
 */
public class VenueManagementDialogController {
    
    @FXML
    private TextField venueNameField;
    
    @FXML
    private TextField locationField;
    
    @FXML
    private TextField capacityField;
    
    @FXML
    private TableView<VenueData> venueTableView;
    
    @FXML
    private TableColumn<VenueData, String> nameColumn;
    
    @FXML
    private TableColumn<VenueData, String> locationColumn;
    
    @FXML
    private TableColumn<VenueData, Integer> capacityColumn;
    
    @FXML
    private TableColumn<VenueData, Void> actionsColumn;
    
    private VenueService venueService;
    
    /**
     * Initialize the dialog.
     */
    @FXML
    public void initialize() {
        venueService = new VenueService();
        
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        // Add delete button to actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            
            {
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px;");
                deleteButton.setPrefWidth(80);
                deleteButton.setOnAction(event -> {
                    VenueData venueData = getTableView().getItems().get(getIndex());
                    handleDeleteVenue(venueData);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(deleteButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });
        
        // Load existing venues
        loadVenues();
    }
    
    /**
     * Load venues from the database.
     */
    private void loadVenues() {
        List<Venue> venues = venueService.getAllVenues();
        ObservableList<VenueData> venueDataList = FXCollections.observableArrayList();
        
        for (Venue venue : venues) {
            venueDataList.add(new VenueData(
                venue.getId(),
                venue.getName(),
                venue.getLocation(),
                venue.getCapacity()
            ));
        }
        
        venueTableView.setItems(venueDataList);
    }
    
    /**
     * Handle add venue button click.
     */
    @FXML
    private void handleAddVenue() {
        String name = venueNameField.getText().trim();
        String location = locationField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        
        // Validate input
        if (name.isEmpty()) {
            showAlert("Validation Error", "Please enter a venue name.", Alert.AlertType.WARNING);
            return;
        }
        
        if (location.isEmpty()) {
            showAlert("Validation Error", "Please enter a location.", Alert.AlertType.WARNING);
            return;
        }
        
        // Parse capacity (optional)
        Integer capacity = null;
        if (!capacityStr.isEmpty()) {
            try {
                capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    showAlert("Validation Error", "Capacity must be a positive number.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Invalid capacity. Please enter a valid number.", Alert.AlertType.WARNING);
                return;
            }
        }
        
        // Create and save venue
        Venue venue = new Venue(name, location, capacity);
        boolean success = venueService.createVenue(venue);
        
        if (success) {
            showAlert("Success", "Venue '" + name + "' has been added successfully!", Alert.AlertType.INFORMATION);
            handleClear();
            loadVenues(); // Refresh the table
        } else {
            showAlert("Error", "Failed to add venue. Please try again.", Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle delete venue.
     */
    private void handleDeleteVenue(VenueData venueData) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Venue?");
        confirm.setContentText("Are you sure you want to delete the venue '" + venueData.getName() + "'?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = venueService.deleteVenue(venueData.getId());
                
                if (success) {
                    showAlert("Success", "Venue deleted successfully!", Alert.AlertType.INFORMATION);
                    loadVenues(); // Refresh the table
                } else {
                    showAlert("Error", "Failed to delete venue. It may be in use by scheduled matches.", Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    /**
     * Handle clear button click.
     */
    @FXML
    private void handleClear() {
        venueNameField.clear();
        locationField.clear();
        capacityField.clear();
    }
    
    /**
     * Handle refresh button click.
     */
    @FXML
    private void handleRefresh() {
        loadVenues();
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) venueNameField.getScene().getWindow();
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
     * Data class for venue information in the table.
     */
    public static class VenueData {
        private final Long id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty location;
        private final SimpleIntegerProperty capacity;
        
        public VenueData(Long id, String name, String location, Integer capacity) {
            this.id = id;
            this.name = new SimpleStringProperty(name);
            this.location = new SimpleStringProperty(location);
            // Use -1 to indicate no capacity set, to distinguish from actual 0 capacity
            this.capacity = new SimpleIntegerProperty(capacity != null ? capacity : -1);
        }
        
        public Long getId() { return id; }
        public String getName() { return name.get(); }
        public String getLocation() { return location.get(); }
        public Integer getCapacity() { 
            int cap = capacity.get();
            return cap == -1 ? null : cap;
        }
        public String getCapacityDisplay() {
            int cap = capacity.get();
            return cap == -1 ? "N/A" : String.valueOf(cap);
        }
    }
}
