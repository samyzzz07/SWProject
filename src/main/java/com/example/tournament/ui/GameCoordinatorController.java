package com.example.tournament.ui;

import com.example.tournament.model.*;
import com.example.tournament.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for GameCoordinator interface.
 * GameCoordinator can perform: manageTimeWindows, recordResult, computeStandings
 */
public class GameCoordinatorController {
    
    @FXML private Label welcomeLabel;
    @FXML private Label statusLabel;
    
    private User currentUser;
    
    @FXML
    public void initialize() {
        statusLabel.setText("Ready");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (Game Coordinator)");
    }
    
    /**
     * Opens dialog to manage time windows for tournaments and matches.
     */
    @FXML
    private void manageTimeWindows() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_time_windows_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Manage Time Windows");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(welcomeLabel.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            
            statusLabel.setText("Time windows management completed");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open time windows dialog: " + e.getMessage());
        }
    }
    
    /**
     * Opens dialog to record match results for tournaments.
     */
    @FXML
    private void recordResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/record_result_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Record Match Result");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(welcomeLabel.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            
            statusLabel.setText("Result recording completed");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open record result dialog: " + e.getMessage());
        }
    }
    
    /**
     * Opens dialog to compute tournament standings.
     */
    @FXML
    private void computeStandings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/compute_standings_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Compute Tournament Standings");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(welcomeLabel.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            
            statusLabel.setText("Standings computation completed");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open compute standings dialog: " + e.getMessage());
        }
    }
    
    /**
     * Opens dialog to manage venues.
     */
    @FXML
    private void manageVenues() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/venue_management_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Venue Management");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(welcomeLabel.getScene().getWindow());
            dialogStage.setScene(new Scene(root, 650, 600));
            dialogStage.showAndWait();
            
            statusLabel.setText("Venue management completed");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open venue management dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Tournament Management System - Login");
            stage.setMaximized(true);  // Maintain maximized mode
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to login screen: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.toLowerCase().contains("error") || title.toLowerCase().contains("fail") 
            ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
